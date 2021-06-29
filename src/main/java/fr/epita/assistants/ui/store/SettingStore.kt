package fr.epita.assistants.ui.store

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
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeThemeEnum.DARK)
    val customThemeDialog: MutableState<Boolean> = mutableStateOf(false)
    val customThemes: SnapshotStateList<IdeTheme> = mutableStateListOf(IdeThemeEnum.CUSTOM)
    val selectedCustomTheme: MutableState<IdeTheme> = mutableStateOf(customThemes[0])

    /**
     * Set the theme
     * @param ideTheme: selected Theme
     */
    fun setTheme(ideTheme: IdeTheme) {
        theme.value = ideTheme
        ideStore.saveConfig()
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

    fun setCustomTheme(onPrimary: Color, primary: Color, onSecondary: Color, secondary: Color, onBackground: Color, background: Color, onSurface: Color, primaryVariant: Color, secondaryVariant: Color) {
        selectedCustomTheme.value.colors = lightColors(
            onPrimary = onPrimary,
            primary = primary,
            onSecondary = onSecondary,
            secondary = secondary,
            onBackground = onBackground,
            background = background,
            onSurface = onSurface,
            primaryVariant = primaryVariant,
            secondaryVariant = secondaryVariant
        )
        // Trick to trigger recomposition, the clean way is to make colors a mutable state
        setTheme(IdeThemeEnum.LIGHT)

        setTheme(selectedCustomTheme.value)
    }

    fun addCustomTheme() {
        val customTheme = CustomTheme(theme.value.colors)

        customThemes.add(customTheme)
        selectCustomTheme(customTheme)
    }

    fun removeCustomTheme(customTheme: IdeTheme) {
        if (customThemes.size <= 1)
            return

        customThemes.remove(customTheme)
        if (customTheme == selectedCustomTheme.value)
            selectCustomTheme(customThemes[0])
    }

    fun selectCustomTheme(customTheme: IdeTheme) {
        selectedCustomTheme.value = customTheme
        setTheme(customTheme)
    }
}