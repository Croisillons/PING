package fr.epita.assistants.ui.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.model.Shortcut
import fr.epita.assistants.ui.model.ShortcutEnum
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.editor.ShortcutView
import java.awt.Cursor

class ShortcutStore(val ideStore: IdeStore) : EditorTab {
    /**
     * Save Shortcut
     */
    var save = Shortcut(ctrl = true, key = Key.S)

    /**
     * Replace Shortcut
     */
    var replace = Shortcut(ctrl = true, key = Key.F)

    /**
     * Open Shortcut tab into editor
     */
    fun openShortcut() {
        ideStore.project.value?.openShortcutEditor(this)
    }

    /**
     * Bind the key event to the given shortcut
     * @param shortcut: The shortcut to change
     * @param event: The keyEvent holding the key combination
     */
    fun set(shortcut: ShortcutEnum, event: KeyEvent) {
        val newShortcut = Shortcut(event.isCtrlPressed, event.isShiftPressed, event.isAltPressed, event.key)
        when (shortcut) {
            ShortcutEnum.SAVE -> save = newShortcut
            ShortcutEnum.REPLACE -> replace = newShortcut
            else -> {
            }
        }
        ideStore.saveConfig()
    }

    /**
     * Reset all shortcut with their default value
     */
    fun resetAll() {
        save = Shortcut(ctrl = true, key = Key.S)
        replace = Shortcut(ctrl = true, key = Key.F)
        ideStore.saveConfig()
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