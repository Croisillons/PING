package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.model.Shortcut
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.IdeCard
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor


@Composable
fun ShortcutsCard(ideStore: IdeStore) {
    val selectedShortcut: MutableState<String> = remember { mutableStateOf("") }
    val focusRequester: FocusRequester = remember { FocusRequester() }
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.focusRequester(focusRequester).onPreviewKeyEvent {
            ideStore.setting.shortcuts.set(selectedShortcut.value, it)
            selectedShortcut.value = ""
            true
        }
    )

    if (selectedShortcut.value != "") {
        focusRequester.requestFocus()
    }

    IdeCard {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.align(Alignment.End)
                        .clickable { ideStore.setting.dismissShortcuts() }
                        .cursor(Cursor.HAND_CURSOR)
                )
                Text(
                    text = "Set your Shortcuts",
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight(500)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ShortcutsItem(
                        "Save",
                        ideStore.setting.shortcuts.save,
                        selectedShortcut.value == "Save"
                    ) { selectedShortcut.value = "Save" }
                    ShortcutsItem(
                        "Replace",
                        ideStore.setting.shortcuts.replace,
                        selectedShortcut.value == "Replace"
                    ) { selectedShortcut.value = "Replace" }
                }
            }
            Button(
                onClick = {
                    ideStore.setting.dismissShortcuts()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                modifier = Modifier.cursor(Cursor.HAND_CURSOR)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Save shortcuts",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun ShortcutsItem(title: String, shortcut: Shortcut, isSelected: Boolean, onClick: () -> Unit) {
    val (hoverState, setHoverState) = remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
            .background(
                if (hoverState or isSelected) MaterialTheme.colors.onSurface else Color.Transparent,
                RoundedCornerShape(4.dp)
            )
            .pointerMoveFilter(
                onEnter = {
                    setHoverState(true)
                    true
                },
                onExit = {
                    setHoverState(false)
                    true
                }
            )
            .cursor(Cursor.HAND_CURSOR)
            .padding(end = 16.dp)
            .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                .padding(vertical = 4.dp, horizontal = 10.dp)
        ) {
            Text(
                text = shortcut.toString(),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}