package fr.epita.assistants.ui.view.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.ProjectStore

/**
 * Display the selected file
 */
@Composable
fun OpenFilesView(projectStore: ProjectStore) {
    val file = projectStore.selectedEditorTab.value
    Column(
        modifier = Modifier.padding(end = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {
        OpenFileTabsView(projectStore)
        if (file != null) {
            file.display(projectStore.ideStore)
        } else {
            NoOpenFileView()
        }
    }
}

/**
 * Display the tab list of open files
 */
@Composable
fun OpenFileTabsView(projectStore: ProjectStore) {
    val horizontalScrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .height(36.dp)
            .horizontalScroll(horizontalScrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (editorTab in projectStore.editorTabs) {
            editorTab.displayTab(
                projectStore.ideStore,
                projectStore.selectedEditorTab.value == editorTab,
                { projectStore.selectEditorTab(editorTab) }
            ) {
                projectStore.closeEditor(editorTab)
            }

        }
    }
    HorizontalScrollbar(
        modifier = Modifier,
        adapter = rememberScrollbarAdapter(horizontalScrollState),
    )
}

@Composable
fun NoOpenFileView() {
    Column(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(12.dp))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Open a file",
            color = MaterialTheme.colors.onSecondary,
            fontSize = 20.sp,
        )
    }
}