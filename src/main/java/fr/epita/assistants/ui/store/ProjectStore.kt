package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.myide.domain.entity.MyProject
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.myide.domain.entity.Project

class ProjectStore(val project: Project) {
    /**
     * List of files of the project
     */
    val tree: MutableState<TreeStore> = mutableStateOf(TreeStore(this))
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

    /**
     * Get Project Name Truncated to 20 char
     */
    fun getTruncatedProjectName(): String {
        var projectName = project.rootNode.path.fileName.toString()
        if (projectName.length > 20)
            projectName = projectName.take(20) + "..."
        return projectName
    }
}