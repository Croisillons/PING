package fr.epita.assistants.ui.store

import fr.epita.assistants.ui.model.ExpandableNode

/**
 * Store for the file tree
 */
class TreeStore(val projectStore: ProjectStore) {
    inner class TreeItem(private val node: ExpandableNode) {
        /**
         * Name of the file/folder
         */
        val name: String get() = node.node.path.fileName.toString()

        /**
         * Depth of the file/folder in the tree
         */
        val depth: Int get() = node.depth

        /**
         * Returns if the node is a folder
         */
        val isFolder: Boolean get() = node.node.isFolder

        /**
         * Returns if the node can be expand
         */
        val canExpand: Boolean get() = node.node.children.isNotEmpty()

        /**
         * Returns if the node is expanded
         */
        val isExpanded: Boolean get() = node.displayedChildren.isNotEmpty()

        /**
         * Returns the extension of the node
         */
        val extension: String get() = node.node.path.fileName.toString().substringAfterLast(".").lowercase()

        /*fun find(): List<TreeItem> {
            val res = mutableListOf<TreeItem>()
            if (canExpand) {
                for (child in node.node.children)
                    res += TreeItem(child).find()
            }
        }*/

        /**
         * Function onClick node
         * If the node is a folder: Expand it
         * If the node is a file: Open it in editor
         */
        fun open() = if (isFolder) node.toggleExpand() else projectStore.openFileEditor(this.node.node, 0)
    }

    /**
     * Root node of the project
     */
    val rootNode: ExpandableNode = ExpandableNode(projectStore.project.rootNode, 0)

    /**
     * List of the the root node children (files and folders)
     */
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

    /*fun find(): List<TreeItem> {
        val res = mutableListOf<TreeItem>()
        for (item in items) {
            res += item.find()
        }
        return res
    }*/
}