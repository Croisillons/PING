package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.v1.DialogProperties
import fr.epita.assistants.ui.store.ProjectStore
import java.io.File
import javax.print.attribute.standard.DateTimeAtCreation
import javax.swing.JFileChooser

@Composable
fun FileActions(showFileActions: MutableState<Boolean>) {
    DropdownMenu(
            expanded = showFileActions.value,
            onDismissRequest = { showFileActions.value = false },
            modifier = Modifier.pointerMoveFilter(
                    onExit = {
                        showFileActions.value = false
                        false
                    }
            )
    ) {
        DropdownMenuItem(
                onClick = { createFile() },
                modifier = Modifier.fillMaxWidth()
        ) {
            Text("New file")
        }
        DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
        ) {
            Text("New Repository")
        }
    }
}


fun createFile() {
    val jChooser = JFileChooser()
    jChooser.dialogTitle = "Create new file"
    jChooser.isAcceptAllFileFilterUsed = false
    if (jChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        jChooser.selectedFile.createNewFile()

    }
}

/*@Composable
fun createFile(projectStore: ProjectStore) {
    val fileName: MutableState<String> = remember { mutableStateOf("") }
    val isFileCreated: MutableState<Boolean> = remember { mutableStateOf(false) }
    if (!isFileCreated.value) {
        AlertDialog(
                onDismissRequest = {
                    projectStore.creatingFile.value = false

                },
                title = { "New File" },
                text = {
                    TextField(
                            value = fileName.value,
                            onValueChange = { fileName.value = it },
                            label = { "Path of your file" }
                    )
                },
                confirmButton = {
                    Button(
                            onClick = {
                                isFileCreated.value = File(fileName.value).createNewFile()
                            },
                            enabled = fileName.value != ""//  && to.value != ""
                    ) {
                        Text(text = "Create file")
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
        )
    }
}*/

