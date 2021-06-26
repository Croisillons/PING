package fr.epita.assistants.ui.store

import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sun.security.auth.NTSidPrimaryGroupPrincipal
import fr.epita.assistants.ui.model.IdeTheme

/**
 * Settings of the IDE
 */
class SettingStore(val ideStore: IdeStore) {
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeTheme.DARK)
    val customThemeDialog: MutableState<Boolean> = mutableStateOf(false)

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
        IdeTheme.CUSTOM.colors = lightColors(
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
        setTheme(IdeTheme.LIGHT)

        setTheme(IdeTheme.CUSTOM)
    }

}