package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.myide.domain.service.MyProjectService
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JPanel

class IdeStore(val projectService: MyProjectService, val setting: SettingStore) : JPanel() {
    var project: MutableState<ProjectStore?> = mutableStateOf(null)

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
        project.value = ProjectStore(projectService.load(Path.of(path)))
    }

    /**
     * Clean Project
     */
    fun cleanProject() {
        if (project.value != null) {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.CLEANUP)
        }
    }

    /**
     * Export Project
     */
    fun exportProject() {
        if (project.value != null) {
            projectService.execute(project.value!!.project, Mandatory.Features.Any.DIST)
        }
    }
}