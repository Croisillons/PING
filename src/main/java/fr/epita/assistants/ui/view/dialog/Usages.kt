package fr.epita.assistants.ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Dialog box to replace all occurrences of a word with an other
 *
 * @param content the whole content of the file
 * @param onValueChange changes the value of the file (see OpenFilesView)
 * @param onReplace indicates if a replacement has been done (see OpenFilesView)
 */

@Composable
fun JumpTo(onSearch: (Boolean) -> Unit, search: (String) -> Unit) {
    val from: MutableState<String> = remember { mutableStateOf("") }
    AlertDialog(
            onDismissRequest = {
                               onSearch(false)
            },
            title = {
            },
            text = {
                Column(modifier = Modifier.padding(top = 10.dp).fillMaxHeight()) {
                    TextField(
                            value = from.value,
                            onValueChange = {
                                from.value = it },
                            label = { Text(text = "Function:") },
                            textStyle = TextStyle(MaterialTheme.colors.onSecondary)
                    )
                }
            },
            confirmButton = {
                Button(
                        onClick = {
                            println(from.value)
                            search(from.value)
                            onSearch(false)
                        },
                        enabled = from.value != ""
                ) {
                    Text(text = "Jump to definition")
                }
            },
            backgroundColor = MaterialTheme.colors.background,
    )
}
