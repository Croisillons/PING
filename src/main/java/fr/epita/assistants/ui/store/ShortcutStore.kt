package fr.epita.assistants.ui.store

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.model.Shortcut
import fr.epita.assistants.ui.model.ShortcutEnum
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.editor.ShortcutView
import fr.epita.assistants.ui.view.editor.ShortcutsItem
import java.awt.Cursor

class ShortcutStore(val ideStore: IdeStore) : EditorTab {
    var save = Shortcut(ctrl = true, key = Key.S)
    var replace = Shortcut(ctrl = true, key = Key.F)

    fun openShortcut() {
        ideStore.project.value?.openShortcutEditor(this)
    }

    fun set(shortcut: ShortcutEnum, event: KeyEvent) {
        val newShortcut = Shortcut(event.isCtrlPressed, event.isShiftPressed, event.isAltPressed, event.key)
        when (shortcut) {
            ShortcutEnum.SAVE -> save = newShortcut
            ShortcutEnum.REPLACE -> replace = newShortcut
            else -> {}
        }
    }

    fun resetAll() {
        save = Shortcut(ctrl = true, key = Key.S)
        replace = Shortcut(ctrl = true, key = Key.F)
    }

    override fun getName(): String {
        return "Shortcuts"
    }

    @Composable
    override fun display(ideStore: IdeStore) {
        ShortcutView(ideStore)
    }

    @Composable
    override fun displayTab(ideStore: IdeStore, isSelected: Boolean, onClick: () -> Unit, onClose: () -> Unit) {
        val hoverState = remember { mutableStateOf(false) }
        Surface(
            color = if (isSelected) MaterialTheme.colors.secondary else if (hoverState.value) MaterialTheme.colors.onSurface else Color.Transparent,
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
                    text = getName(),
                    color = MaterialTheme.colors.onSecondary
                )

                Icon(
                    Icons.Default.Close,
                    tint = MaterialTheme.colors.onSecondary,
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

}