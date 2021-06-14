package fr.epita.assistants.ui.view.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.store.OpenFileStore
import fr.epita.assistants.ui.store.ProjectStore

@Composable
fun OpenFilesView(projectStore: ProjectStore) {
    Column (modifier = Modifier.background(MaterialTheme.colors.background)) {
        OpenFileTabsView(projectStore)
        if (projectStore.selectedOpenFile.value != null) {
            EditorView(
                projectStore.selectedOpenFile.value!!.content.value
            ) { projectStore.selectedOpenFile.value!!.content.value = it }
        }
    }
}


@Composable
fun OpenFileTabsView(projectStore: ProjectStore) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (openFileStore in projectStore.openFiles) {
            OpenFileTab(openFileStore, { projectStore.selectOpenFile(openFileStore) }) { openFileStore.close() }
        }
    }
}

@Composable
fun OpenFileTab(openFileStore: OpenFileStore, onClick: () -> Unit, onClose: () -> Unit) {
    val hoverState = remember { mutableStateOf(false) }
    Surface(
        color = if (hoverState.value or openFileStore.selected) MaterialTheme.colors.onSurface else Color.Transparent,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
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
            .padding(end = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = openFileStore.filename,
                color = MaterialTheme.colors.onSecondary
            )
            Icon(
                Icons.Default.Close,
                tint = if (hoverState.value) MaterialTheme.colors.onSecondary else Color.Transparent,
                contentDescription = "Close Tab",
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
                    .clickable(onClick = onClose)
            )
        }
    }
}

@Composable
fun EditorView(content: String, onValueChange: (String) -> Unit) {
    SelectionContainer {
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = content,
                onValueChange = onValueChange,
                textStyle = TextStyle(MaterialTheme.colors.onSecondary),
                modifier = Modifier.padding(horizontal = 8.dp)
                    .fillMaxHeight(),
            )
        }
    }
}