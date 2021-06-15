package fr.epita.assistants.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.epita.assistants.myide.domain.entity.Node

class ExpandableNode(val node: Node, val depth: Int) {
    var displayedChildren: List<ExpandableNode> by mutableStateOf(emptyList())

    /**
     * Display or hide folder's children
     */
    fun toggleExpand() {
        if (displayedChildren.isEmpty()) {
            displayedChildren = node.children.map { ExpandableNode(it, depth + 1) }
                .sortedWith(compareBy({ it.node.isFolder }, { it.node.path.fileName.toString() }))
                .sortedBy { !it.node.isFolder }
        } else {
            displayedChildren = emptyList()
        }
    }

}