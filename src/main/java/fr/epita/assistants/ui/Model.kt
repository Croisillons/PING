package fr.epita.assistants.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.entity.MyProject
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.store.OpenFileStore

class ProjectStore(val project: MyProject) {
    /**
     * List of tab displaying a file content
     */
    val openFiles: MutableList<OpenFileStore> = mutableStateListOf()

    /**
     * Currently viewed file tab
     */
    var selectedOpenFile: MutableState<OpenFileStore?> = mutableStateOf(null)

    /**
     * Select an open file tab
     */
    fun selectOpenFile(editor: OpenFileStore?) {
        selectedOpenFile.value = editor
    }

    /**
     * Open a file to a tab and select it
     */
    fun openFileToEditor(node: Node) {
        var editor: OpenFileStore = OpenFileStore(node, this)

        if (openFiles.none { it.node == node }) {
            openFiles.add(editor)
        } else {
            editor = openFiles.first { it.node == node }
        }
        selectOpenFile(editor)
    }

    /**
     * Close an open file tab
     */
    fun closeEditor(editor: OpenFileStore) {
        val index = openFiles.indexOf(editor)
        openFiles.remove(editor)
        selectOpenFile(openFiles.getOrNull(index.coerceAtMost(openFiles.lastIndex)))
    }
}

class Settings {
}
