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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.store.TreeStore
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.dialog.FileActions
import java.awt.Cursor

/**
 * TopBar and files tree
 * @param projectStore stores the project
 */
@Composable
fun TreeView(projectStore: ProjectStore) {
    Column(modifier = Modifier.width(projectStore.treeWidth.value).padding(start = 8.dp)) {
        TreeTopBarView(projectStore, projectStore.getTruncatedProjectName(), {})
        HierarchyView(projectStore.tree.value)
    }
}

/**
 * TopBar with name of the project, refresh and files action button
 * @param projectStore stores the project
 * @param projectName name of the project
 * @param onRefresh refreshes the files tree
 */
@Composable
fun TreeTopBarView(projectStore: ProjectStore, projectName: String, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(44.dp)
            .background(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = projectName,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSecondary
        )

        Row {
            Icon(
                Icons.Default.Refresh,
                tint = MaterialTheme.colors.onSecondary,
                contentDescription = "Refresh project tree",
                modifier = Modifier
                    .size(26.dp)
                    .padding(horizontal = 4.dp)
                    .defaultMinSize(minWidth = 26.dp)
                    .clickable {}
            )
            Icon(
                Icons.Default.Add,
                tint = MaterialTheme.colors.onSecondary,
                contentDescription = "New Project",
                modifier = Modifier
                    .size(26.dp)
                    .padding(horizontal = 4.dp)
                    .defaultMinSize(minWidth = 26.dp)
                    .clickable {projectStore.showFileActions.value = true}
            )
            FileActions(projectStore.showFileActions)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * Files tree
 * @param treeStore stores the tree of files and folder
 */
@Composable
fun HierarchyView(treeStore: TreeStore) {
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.primary, RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        val verticalScrollState = rememberLazyListState()
        val horizontalScrollState = rememberLazyListState()

        LazyRow(
            Modifier.fillMaxSize().padding(12.dp),
            horizontalScrollState,
        ) {
            item {
                LazyColumn(
                    Modifier.fillMaxSize().padding(end = 12.dp),
                    verticalScrollState,
                ) {
                    items(treeStore.items.size) { idx ->
                        HierarchyItemView(treeStore.items[idx])
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

/**
 *
 */
@Composable
fun HierarchyItemView(node: TreeStore.TreeItem) {
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
            )
            .cursor(Cursor.HAND_CURSOR),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HierarchyItemIconView(node)
        Text(
            text = node.name,
            color = MaterialTheme.colors.onPrimary,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(end = 8.dp)
        )
    }

}

/**
 * Icons of each item
 * @param node file or folder
 */
@Composable
fun HierarchyItemIconView(node: TreeStore.TreeItem) {
    if (node.isFolder) {
        when {
            !node.canExpand -> Icon(
                Icons.Default.Folder,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onPrimary
            )
            node.isExpanded -> Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onPrimary
            )
            else -> Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = node.name,
                tint = MaterialTheme.colors.onPrimary
            )
        }
    } else {
        Icon(Icons.Outlined.Description, contentDescription = node.name, tint = MaterialTheme.colors.onPrimary)
    }
}