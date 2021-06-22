package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import fr.epita.assistants.ui.model.IdeTheme

/**
 * Settings of the IDE
 */
class SettingStore {
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeTheme.DARK)
    val customThemeDialog: MutableState<Boolean> = mutableStateOf(false)

    /**
     * Set the theme
     * @param ideTheme: selected Theme
     */
    fun setTheme(ideTheme: IdeTheme) {
        theme.value = ideTheme
    }

    /**
     * Open dialog to set custom theme
     */
    fun openCustomTheme() {
        customThemeDialog.value = true
    }

    fun dimissCustomTheme() {
        customThemeDialog.value = false
    }

    fun setCustomTheme(onPrimary: String, primary: String, onSecondary: String, secondary: String, onBackground: String, background: String, onSurface: String, primaryVariant: String, secondaryVariant: String) {
        customThemeDialog.value = false
    }
}