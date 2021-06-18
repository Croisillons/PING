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
import fr.epita.assistants.ui.store.OpenFileStore
import fr.epita.assistants.ui.store.ProjectStore
import java.io.File
import java.nio.file.Path
import javax.print.attribute.standard.DateTimeAtCreation
import javax.swing.JFileChooser

/**
 * Dropdown menu for file and folder actions (creation, renaming, deletion)
 *
 * @param showFileActions if true, the dropDownMenu is expandeded
 */

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

/**
 * Create a new file. If this is a .java, create a snippet of code for the class.
 */

fun createFile() {
    val jChooser = JFileChooser()
    jChooser.dialogTitle = "Create new file"
    jChooser.isAcceptAllFileFilterUsed = false
    if (jChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        jChooser.selectedFile.createNewFile()
        val file = jChooser.selectedFile
        if (jChooser.selectedFile.toString().contains(".java")) {
            File("src/main/java/fr/epita/assistants/ui/utils/toto.java").copyTo(File(file.toString()), true)
            file.writeText(file.readText().replace("toto", jChooser.selectedFile.name.removeSuffix(".java")))
        }
    }
}

