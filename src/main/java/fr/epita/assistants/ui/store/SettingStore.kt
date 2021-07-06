package fr.epita.assistants.ui.store

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import fr.epita.assistants.ui.model.CustomTheme
import fr.epita.assistants.ui.model.IdeTheme
import fr.epita.assistants.ui.model.IdeThemeEnum

/**
 * Settings of the IDE
 */
class SettingStore(val ideStore: IdeStore) {
    /**
     * Everything related to the IDE Themes
     */
    val theme: ThemeStore = ThemeStore(ideStore)

    /**
     * Everything related t o the IDE Shortcuts
     */
    val shortcuts: ShortcutStore = ShortcutStore(ideStore)
}