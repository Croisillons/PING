package fr.epita.assistants.ui.view.tree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.store.ProjectStore

class ExpandableNode(val node: Node, val depth: Int) {
    var displayedChildren: List<ExpandableNode> by mutableStateOf(emptyList())
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

class Tree(val projectStore: ProjectStore) {
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

@Composable
fun TreeView(projectStore: ProjectStore) {
    Column(modifier = Modifier.width(300.dp)) {
        TreeTopBarView(projectStore.project.rootNode.path.fileName.toString(), {})
        HierarchyView(Tree(projectStore))
    }
}

@Composable
fun TreeTopBarView(projectName: String, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = projectName
        )

        Icon(
            Icons.Default.Refresh,
            tint = LocalContentColor.current,
            contentDescription = "Refresh project tree",
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
                .clickable {}
        )
        Icon(
            Icons.Default.Add,
            tint = LocalContentColor.current,
            contentDescription = "New Project",
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
                .clickable {}
        )
    }
}

@Composable
fun HierarchyView(tree: Tree) {
    Box(
        modifier = Modifier.padding(8.dp)
    ) {
        val verticalScrollState = rememberLazyListState()

        LazyColumn(
            Modifier.fillMaxSize().padding(end = 12.dp),
            verticalScrollState,
        ) {
            items(tree.items.size) { idx ->
                HierarchyItemView(tree.items[idx])
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Composable
fun HierarchyItemView(node: Tree.TreeItem) {
    val hoverState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 24.dp * node.depth)
            .clickable { node.open() }
            .background(
                if (hoverState.value) Color.LightGray else Color(135, 135, 135, 40),
                RoundedCornerShape(4.dp)
            )
            .pointerMoveFilter(
                onEnter = {
                    hoverState.value = true
                    false
                },
                onExit = {
                    hoverState.value = false
                    false
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HierarchyItemIconView(node)
        Text(
            text = node.name,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }

}

@Composable
fun HierarchyItemIconView(node: Tree.TreeItem) {
    if (node.isFolder) {
        when {
            !node.canExpand -> Unit
            node.isExpanded -> Icon(Icons.Default.KeyboardArrowDown, contentDescription = node.name)
            else -> Icon(Icons.Default.KeyboardArrowRight, contentDescription = node.name)
        }
    } else {


        Icon(Icons.Outlined.Description, contentDescription = node.name)
    }
}