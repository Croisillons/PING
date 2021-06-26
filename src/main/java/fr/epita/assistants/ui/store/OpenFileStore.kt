package fr.epita.assistants.ui.store

import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.entity.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Store an open file
 */
class OpenFileStore(val node: Node, val projectStore: ProjectStore) {
    val filename: String = node.path.fileName.toString()
    val content = mutableStateOf("")
    val hasChanged = mutableStateOf(false)
    val selected: Boolean
        get() {
            return projectStore.selectedOpenFile.value == this
        }

    /**
     * Close the file editor
     */
    fun close() {
        projectStore.closeEditor(this)
    }

    init {
        loadFileContent()
    }

    /**
     * Open and read file content in a Coroutine Scope
     */
    fun loadFileContent() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val result = node.content
            launch(Dispatchers.Main) {
                content.value = result
            }
        }
    }

}