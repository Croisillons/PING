package fr.epita.assistants.ui.view.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.OpenFileStore
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.utils.CodeHighlight
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.dialog.Sed
import java.awt.Cursor

/**
 * Display the selected file
 */
@Composable
fun OpenFilesView(projectStore: ProjectStore) {
    val sedState = remember { mutableStateOf(false) }
    val file = projectStore.selectedOpenFile.value
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        OpenFileTabsView(projectStore)
        if (file != null) {
            val onValueChange: (it: String) -> Unit = {
                file.hasChanged.value = true
                file.content.value = it
            }

            val onReplace: (it: Boolean) -> Unit = {
                sedState.value = it
            }

            EditorView(
                file.content.value,
                onValueChange,
                onReplace,
            ) { projectStore.saveFile() }

            if (sedState.value) {
                Sed(file.content.value, onValueChange, onReplace)
            }

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
                if (!hoverState.value && openFileStore.hasChanged.value) Icons.Default.Circle else Icons.Default.Close,
                tint = if (hoverState.value || openFileStore.hasChanged.value) MaterialTheme.colors.onSecondary else Color.Transparent,
                contentDescription = "Close Tab",
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 4.dp)
                    .clickable(onClick = onClose)
                    .align(Alignment.CenterVertically)
                    .cursor(Cursor.HAND_CURSOR)
            )

        }
    }
}

@Composable
fun EditorView(content: String, onValueChange: (String) -> Unit, onReplace: (Boolean) -> Unit, onSave: () -> Unit) {
    SelectionContainer {
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = content,
                onValueChange = onValueChange,
                textStyle = TextStyle(MaterialTheme.colors.onSecondary, fontWeight = FontWeight(600), fontFamily = FontFamily.Monospace),
                modifier = Modifier.padding(horizontal = 8.dp)
                    .fillMaxHeight()
                    .onPreviewKeyEvent {
                        when {
                            (it.isCtrlPressed && it.key == Key.S) -> {
                                onSave()
                                true
                            }
                            (it.isCtrlPressed && it.key == Key.F) -> {
                                onReplace(true)
                                true
                            }
                            else -> false
                        }
                    },
                visualTransformation = CodeHighlight(MaterialTheme.colors)
            )
        }
    }
}

@Composable
fun NoOpenFileView() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.secondary),
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