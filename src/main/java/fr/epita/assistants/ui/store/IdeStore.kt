package fr.epita.assistants.ui.store

import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.Settings
import java.nio.file.Path

class IdeStore(val projectService: MyProjectService, val setting: SettingStore) {
    lateinit var project: ProjectStore

    fun loadProject(path: String) {
        project = ProjectStore(projectService.load(Path.of(path)))
    }
}