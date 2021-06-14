package fr.epita.assistants.ui.store

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.Settings
import java.awt.FileDialog
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JPanel

class IdeStore(val projectService: MyProjectService, val setting: SettingStore) : JPanel() {
    var project: MutableState<ProjectStore?> = mutableStateOf(null)

    fun openProject() {
        val jChooser = JFileChooser()
        jChooser.dialogTitle = "Open Project Folder"
        jChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        jChooser.isAcceptAllFileFilterUsed = false
        if (jChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadProject(jChooser.selectedFile.toString())
        }
    }


    fun loadProject(path: String) {
        project.value = ProjectStore(projectService.load(Path.of(path)))
    }
}