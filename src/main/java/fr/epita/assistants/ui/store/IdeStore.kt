package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.imageFromResource
import fr.epita.assistants.myide.domain.entity.Feature
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.myide.domain.service.MyProjectService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JPanel

class IdeStore(val projectService: MyProjectService, val setting: SettingStore) : JPanel() {
    var project: MutableState<ProjectStore?> = mutableStateOf(null)
    val snackBar: SnackBarStore = SnackBarStore()

    /**
     * Open Project
     */
    fun openProject() {
        val jChooser = JFileChooser()
        jChooser.dialogTitle = "Open Project Folder"
        jChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        jChooser.isAcceptAllFileFilterUsed = false
        if (jChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadProject(jChooser.selectedFile.toString())
        }
    }

    /**
     * Load Project
     */
    fun loadProject(path: String) {
        project.value = ProjectStore(this, projectService.load(Path.of(path)))
    }

    /**
     * Clean Project
     */
    fun cleanProject() {
        project.value?.let {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.CLEANUP)
        }
    }

    /**
     * Export Project
     */
    fun exportProject() {
        project.value?.let {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.DIST)
        }
    }


    fun compileProject() {
        project.value?.let {
            val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                val result: Feature.ExecutionReport = projectService.execute(project.value!!.project, Mandatory.Features.Maven.COMPILE)
                launch(Dispatchers.Main) {
                    if (result.isSuccess) {
                        // Display happy cowboy and Hiyaa
                        snackBar.title.value = "Compilation succeed."
                        snackBar.image.value = snackBar.successImage
                    }
                    else {
                        // Display sad cowboy and Hiyaa
                        snackBar.title.value = "Compilation failed."
                        snackBar.image.value = snackBar.failImage
                    }
                    snackBar.launchSnackBar()
                }
            }

        }
    }
}