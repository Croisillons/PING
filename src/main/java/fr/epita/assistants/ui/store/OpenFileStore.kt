package fr.epita.assistants.ui.store

import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.ProjectStore

class OpenFileStore(val node: Node, val projectStore: ProjectStore) {
    val filename: String = node.path.fileName.toString()
    val content = mutableStateOf("File:$filename \neof")

    fun close() {
        projectStore.closeEditor(this)
    }

    val selected: Boolean
        get() {
            return projectStore.selectedOpenFile.value == this
        }

}