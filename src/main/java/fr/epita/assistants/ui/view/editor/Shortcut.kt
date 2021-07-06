package fr.epita.assistants.ui.view.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.model.Shortcut
import fr.epita.assistants.ui.model.ShortcutEnum
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor

@Composable
fun ShortcutView(ideStore: IdeStore) {
    val selectedShortcut = remember { mutableStateOf(ShortcutEnum.NONE) }
    val verticalScrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(12.dp))
            .padding(32.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set your Shortcuts",
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 24.sp,
                fontWeight = FontWeight(500)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Reset",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.align(Alignment.End)
                    .padding(end = 16.dp, bottom = 10.dp)
                    .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp, horizontal = 10.dp)
                    .clickable {
                        ideStore.setting.shortcuts.resetAll()
                        selectedShortcut.value = ShortcutEnum.SAVE
                        selectedShortcut.value = ShortcutEnum.REPLACE
                        selectedShortcut.value = ShortcutEnum.NONE
                    },
            )
            Divider(color = MaterialTheme.colors.primary, thickness = 0.5.dp, modifier = Modifier.padding(bottom = 10.dp))
            ShortcutsItem(
                "Save",
                ideStore.setting.shortcuts.save,
                selectedShortcut.value == ShortcutEnum.SAVE,
                { selectedShortcut.value = ShortcutEnum.SAVE }) {
                ideStore.setting.shortcuts.set(ShortcutEnum.SAVE, it)
                selectedShortcut.value = ShortcutEnum.NONE
            }
            ShortcutsItem(
                    "Replace",
                    ideStore.setting.shortcuts.replace,
                    selectedShortcut.value == ShortcutEnum.REPLACE,
                    { selectedShortcut.value = ShortcutEnum.REPLACE }) {
                ideStore.setting.shortcuts.set(ShortcutEnum.REPLACE, it)
                selectedShortcut.value = ShortcutEnum.NONE
            }
            ShortcutsItem(
                    "Jump to definition",
            ideStore.setting.shortcuts.jumpTo,
            selectedShortcut.value == ShortcutEnum.JUMP_TO,
            { selectedShortcut.value = ShortcutEnum.JUMP_TO }) {
            ideStore.setting.shortcuts.set(ShortcutEnum.JUMP_TO, it)
            selectedShortcut.value = ShortcutEnum.NONE
        }


        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}

@Composable
fun ShortcutsItem(
    title: String,
    shortcut: Shortcut,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSet: (KeyEvent) -> Unit
) {
    val (hoverState, setHoverState) = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
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
                    false
                },
                onExit = {
                    setHoverState(false)
                    false
                }
            )
            .padding(end = 16.dp)
            .clickable {
                onClick()
                focusRequester.requestFocus()
            },
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
            BasicTextField(
                value = if (isSelected) "Recording..." else shortcut.toString(),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.width(IntrinsicSize.Max)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.toString() == "Active") {
                            onClick()
                        }
                    }
                    .onPreviewKeyEvent {
                        when {
                            ((it.isCtrlPressed || it.isShiftPressed || it.isAltPressed)
                                    && it.key != Key.CtrlLeft && it.key != Key.ShiftLeft && it.key != Key.AltLeft
                                    && it.key != Key.CtrlRight && it.key != Key.ShiftRight && it.key != Key.AltRight
                                    && it.key.keyCode != 0L && isSelected) -> {
                                onSet(it)
                                true
                            }
                            else -> false
                        }
                    },
                textStyle = TextStyle(MaterialTheme.colors.onPrimary)
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}