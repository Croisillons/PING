package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
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

    fun setCustomTheme(value: Color, value1: Color, value2: Color, value3: Color, value4: Color, value5: Color, value6: Color, value7: Color, value8: Color) {

    }

}