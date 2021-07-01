package fr.epita.assistants.ui.model

import androidx.compose.ui.input.key.*

class Shortcut(val ctrl: Boolean = false, val shift: Boolean = false, val alt: Boolean = false, val key: Key) {
    fun isPressed(event: KeyEvent) : Boolean {
        return event.isCtrlPressed == ctrl
                && event.isShiftPressed == shift
                && event.isAltPressed == alt
                && event.key == key
    }

    override fun toString(): String {
        var res: String = ""
        if (ctrl) res += "Ctrl + "
        if (shift) res += "Shift + "
        if (alt) res += "Alt + "
        res += key.toString().removePrefix("Key: ")
        return res
    }
}