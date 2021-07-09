package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Settings of the IDE
 */
class SettingStore(val ideStore: IdeStore) {
    /**
     * Everything related to the IDE Themes
     */
    val theme: ThemeStore = ThemeStore(ideStore)

    /**
     * Everything related to the IDE Shortcuts
     */
    val shortcuts: ShortcutStore = ShortcutStore(ideStore)

    /**
     * Vim Mode
     */
    val vimMode: MutableState<Boolean> = mutableStateOf(true)

    fun toggleVimMode() {
        vimMode.value = !vimMode.value
    }
}
