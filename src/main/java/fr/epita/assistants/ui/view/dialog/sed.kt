package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun Sed(content: String, onValueChange: (String) -> Unit, onReplace: (Boolean) -> Unit) {
    val from: MutableState<String> = remember { mutableStateOf("") }
    val to: MutableState<String> = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onReplace(false)
        },
        title = {
        },
        text = {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                TextField(
                    value = from.value,
                    onValueChange = { from.value = it },
                    label = { Text(text = "From:") },
                    textStyle = TextStyle(MaterialTheme.colors.onSecondary)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = to.value,
                    onValueChange = { to.value = it },
                    label = { Text(text = "To:") },
                    textStyle = TextStyle(MaterialTheme.colors.onSecondary)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onValueChange(content.replace(from.value, to.value))
                    onReplace(false)
                },
                enabled = from.value != ""//  && to.value != ""
            ) {
                Text(text = "Replace")
            }
        },
        backgroundColor = MaterialTheme.colors.background,
    )
}
