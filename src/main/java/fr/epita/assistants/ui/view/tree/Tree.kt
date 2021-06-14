package fr.epita.assistants.ui.view.tree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.model.Tree
import fr.epita.assistants.ui.store.ProjectStore

@Composable
fun TreeView(projectStore: ProjectStore) {
    val tree: MutableState<Tree> = remember { mutableStateOf(Tree(projectStore)) }
    Column(modifier = Modifier.width(300.dp).background(MaterialTheme.colors.primary)) {
        TreeTopBarView(projectStore.project.rootNode.path.fileName.toString(), {})
        HierarchyView(tree.value)
    }
}

@Composable
fun TreeTopBarView(projectName: String, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = projectName,
            color = MaterialTheme.colors.onPrimary
        )

        Icon(
            Icons.Default.Refresh,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = "Refresh project tree",
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
                .clickable {}
        )
        Icon(
            Icons.Default.Add,
            tint = MaterialTheme.colors.onPrimary,
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
        modifier = Modifier.padding(8.dp).fillMaxWidth().background(MaterialTheme.colors.primary)
    ) {
        val verticalScrollState = rememberLazyListState()
        val horizontalScrollState = rememberLazyListState()

        LazyRow(
            Modifier.fillMaxSize().padding(end = 12.dp),
            horizontalScrollState,
        ) {
            item {
                LazyColumn(
                    Modifier.fillMaxSize().padding(end = 12.dp),
                    verticalScrollState,
                ) {
                    items(tree.items.size) { idx ->
                        HierarchyItemView(tree.items[idx])
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
        )
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(horizontalScrollState),
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(end = 12.dp)
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
                if (hoverState.value) MaterialTheme.colors.onSurface else Color.Transparent,
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
            color = MaterialTheme.colors.onSecondary,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 8.dp)
        )
    }

}

@Composable
fun HierarchyItemIconView(node: Tree.TreeItem) {
    if (node.isFolder) {
        when {
            !node.canExpand -> Icon(
                Icons.Default.Folder,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onSecondary
            )
            node.isExpanded -> Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onSecondary
            )
            else -> Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onSecondary
            )
        }
    } else {
        Icon(Icons.Outlined.Description, contentDescription = node.name, tint = MaterialTheme.colors.onSecondary)
    }
}