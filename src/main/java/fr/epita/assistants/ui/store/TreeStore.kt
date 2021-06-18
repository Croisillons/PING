package fr.epita.assistants.ui.store

import fr.epita.assistants.ui.model.ExpandableNode

/**
 * Store for the file tree
 */
class TreeStore(val projectStore: ProjectStore) {
    inner class TreeItem(private val node: ExpandableNode) {
        val name: String get() = node.node.path.fileName.toString()
        val depth: Int get() = node.depth
        val isFolder: Boolean get() = node.node.isFolder
        val canExpand: Boolean get() = node.node.children.isNotEmpty()
        val isExpanded: Boolean get() = node.displayedChildren.isNotEmpty()
        val extension: String get() = node.node.path.fileName.toString().substringAfterLast(".").lowercase()
        fun open() = if (isFolder) node.toggleExpand() else projectStore.openFileEditor(this.node.node)
    }

    val rootNode: ExpandableNode = ExpandableNode(projectStore.project.rootNode, 0)
    val items: List<TreeItem> get() = rootNode.toItems()

    /**
     * List all tree into a list or TreeItem
     */
    fun ExpandableNode.toItems(): List<TreeItem> {
        val list = mutableListOf<TreeItem>()
        fun ExpandableNode.recAdd(list: MutableList<TreeItem>) {
            list.add(TreeItem(this))
            for (child in displayedChildren) {
                child.recAdd(list)
            }
        }

        recAdd(list)
        return list
    }
}