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
    fun openFileEditor(node: Node) {
        var editor: OpenFileStore? = openFiles.firstOrNull { it.node == node}

        if (editor == null) {
            editor = OpenFileStore(node, this)
            openFiles.add(editor)
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
