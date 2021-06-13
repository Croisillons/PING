package fr.epita.assistants.ui.store

import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.ProjectStore
import fr.epita.assistants.ui.Settings

class IdeStore(val projectService: MyProjectService, val setting: Settings) {
    lateinit var project: ProjectStore

}