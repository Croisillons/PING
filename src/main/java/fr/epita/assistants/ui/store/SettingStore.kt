package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.ui.model.IdeTheme

/**
 * Settings of the IDE
 */
class SettingStore {
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeTheme.DARK)

    /**
     * Set the theme
     * @param ideTheme: selected Theme
     */
    fun setTheme(ideTheme: IdeTheme) {
        theme.value = ideTheme
    }
}