package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.utils.MenuBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JPanel


/**
 * Stores a ProjectService. Adapter from frontend to backend
 */
class IdeStore(val projectService: MyProjectService) {
    val setting: SettingStore = SettingStore(this)
    var project: MutableState<ProjectStore?> = mutableStateOf(null)
    val menuBarState: MutableState<MenuBar?> = mutableStateOf(null)

    /**
     * Open a project by showing a file explorer and loading the chosen project
     */
    fun openProject() {
        val jChooser = JFileChooser()
        jChooser.dialogTitle = "Open Project Folder"
        jChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        jChooser.isAcceptAllFileFilterUsed = false
        if (jChooser.showOpenDialog(JPanel()) == JFileChooser.APPROVE_OPTION) {
            loadProject(jChooser.selectedFile.toString())
        }
    }

    /**
     * Load a project from a path
     * @param path Path to the project root
     */
    fun loadProject(path: String) {
        project.value = ProjectStore(this, projectService.load(Path.of(path)))
        saveConfig()
    }

    /**
     * Clean a project, deleting all files specified in .myideignore
     */
    fun cleanProject() {
        project.value?.let {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.CLEANUP)
            project.value!!.snackBar.title.value = "Clean succeed."
            project.value!!.snackBar.launchSnackBar()
        }
    }

    /**
     * Clean the project and export it to a zip file
     */
    fun exportProject() {
        project.value?.let {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.DIST)
            project.value!!.snackBar.title.value = "Export succeed."
            project.value!!.snackBar.launchSnackBar()
        }
    }

    /**
     * Save the configuration of the IDE: View size, current project, current theme, current shortcuts
     * Done in background using Coroutine
     */
    fun saveConfig() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                FileOutputStream("./config.properties").use { output ->
                    val prop = Properties()

                    // set the properties value
                    project.value?.let {
                        prop.setProperty("project.path", project.value!!.project.rootNode.path.toString())
                        prop.setProperty("ide.width", project.value!!.treeWidth.value.value.toString())
                        prop.setProperty("ide.height", project.value!!.filesHeight.value.value.toString())
                    }

                    val colors = setting.theme.theme.value.colors
                    prop.setProperty("theme.name", setting.theme.theme.value.themeName.value)
                    prop.setProperty("theme.onPrimary", colors.onPrimary.toArgb().toString())
                    prop.setProperty("theme.primary", colors.primary.toArgb().toString())
                    prop.setProperty("theme.onSecondary", colors.onSecondary.toArgb().toString())
                    prop.setProperty("theme.secondary", colors.secondary.toArgb().toString())
                    prop.setProperty("theme.onBackground", colors.onBackground.toArgb().toString())
                    prop.setProperty("theme.background", colors.background.toArgb().toString())
                    prop.setProperty("theme.onSurface", colors.onSurface.toArgb().toString())
                    prop.setProperty("theme.primaryVariant", colors.primaryVariant.toArgb().toString())
                    prop.setProperty("theme.secondaryVariant", colors.secondaryVariant.toArgb().toString())

                    setting.theme.customThemes.forEachIndexed { idx, theme ->
                        prop.setProperty("customTheme$idx.name", theme.themeName.value)
                        prop.setProperty("customTheme$idx.onPrimary", theme.colors.onPrimary.toArgb().toString())
                        prop.setProperty("customTheme$idx.primary", theme.colors.primary.toArgb().toString())
                        prop.setProperty("customTheme$idx.onSecondary", theme.colors.onSecondary.toArgb().toString())
                        prop.setProperty("customTheme$idx.secondary", theme.colors.secondary.toArgb().toString())
                        prop.setProperty("customTheme$idx.onBackground", theme.colors.onBackground.toArgb().toString())
                        prop.setProperty("customTheme$idx.background", theme.colors.background.toArgb().toString())
                        prop.setProperty("customTheme$idx.onSurface", theme.colors.onSurface.toArgb().toString())
                        prop.setProperty(
                            "customTheme$idx.primaryVariant",
                            theme.colors.primaryVariant.toArgb().toString()
                        )
                        prop.setProperty(
                            "customTheme$idx.secondaryVariant",
                            theme.colors.secondaryVariant.toArgb().toString()
                        )
                    }

                    prop.setProperty("shortcut.save", setting.shortcuts.save.toString())
                    prop.setProperty("shortcut.replace", setting.shortcuts.replace.toString())

                    // save properties to project root folder
                    prop.store(output, null)
                    // println("Configuration saved : $prop")
                }
            } catch (io: IOException) {
                println("Failed to save configuration")
            }
        }
    }
}