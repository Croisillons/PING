package fr.epita.assistants.ui.model

import androidx.compose.ui.input.key.*

/**
 * A shortcut is composed of a Key and at least one of the three: ctrl/shift/alt
 */
class Shortcut(val ctrl: Boolean = false, val shift: Boolean = false, val alt: Boolean = false, val key: Key) {
    /**
     * Checks if the KeyEvent corresponds to the shortcut
     * @param event: the KeyEvent
     * @return is the keyEvent corresponds to the shortcut
     */
    fun isPressed(event: KeyEvent): Boolean {
        return event.isCtrlPressed == ctrl
                && event.isShiftPressed == shift
                && event.isAltPressed == alt
                && event.key == key
    }

    /**
     * Override toString to display the key combination
     * @return the shortcut as a string
     */
    override fun toString(): String {
        var res: String = ""
        if (ctrl) res += "Ctrl + "
        if (shift) res += "Shift + "
        if (alt) res += "Alt + "
        res += key.toString().removePrefix("Key: ")
        return res
    }
}