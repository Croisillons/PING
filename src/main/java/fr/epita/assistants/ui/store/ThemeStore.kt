package fr.epita.assistants.ui.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.model.CustomTheme
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.model.IdeTheme
import fr.epita.assistants.ui.model.IdeThemeEnum
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.dialog.ThemeView
import java.awt.Cursor

class ThemeStore(val ideStore: IdeStore) : EditorTab {
    /**
     * Mutable state of the current theme of the IDE
     */
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeThemeEnum.DARK)

    /**
     * Mutable state list of all the custom themes
     */
    val customThemes: SnapshotStateList<IdeTheme> = mutableStateListOf(IdeThemeEnum.CUSTOM)

    /**
     * Mutable state of the selected custom Theme in the Theme Tab
     */
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
     * Open Theme tab into the editor
     */
    fun openCustomTheme() {
        ideStore.project.value?.openThemeEditor(this)
    }

    /**
     * Remmove Theme tab from the editor
     */
    fun dismissCustomTheme() {
        ideStore.project.value?.closeEditor(this)
    }

    /**
     * Set Custom Theme as current one
     */
    fun setCustomTheme(
        onPrimary: Color,
        primary: Color,
        onSecondary: Color,
        secondary: Color,
        onBackground: Color,
        background: Color,
        onSurface: Color,
        primaryVariant: Color,
        secondaryVariant: Color
    ) {
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

    /**
     * Add a new custom Theme that as the color of the current theme
     */
    fun addCustomTheme() {
        val customTheme = CustomTheme("Theme #${customThemes.size}", theme.value.colors)

        customThemes.add(customTheme)
        selectCustomTheme(customTheme)
    }

    /**
     * Remove a custom Theme from customThemes
     * @param customTheme: the theme to remove
     */
    fun removeCustomTheme(customTheme: IdeTheme) {
        if (customThemes.size <= 1)
            return

        customThemes.remove(customTheme)
        if (customTheme == selectedCustomTheme.value)
            selectCustomTheme(customThemes[0])
    }

    /**
     * Select the given customTheme
     * @param customTheme: the theme to select
     */
    fun selectCustomTheme(customTheme: IdeTheme) {
        selectedCustomTheme.value = customTheme
        setTheme(customTheme)
    }

    /**
     * Load custom Theme from config file
     * @param name: name of the theme
     * @param colors: Colors of the theme
     */
    fun loadCustomTheme(name: String, colors: Colors) {
        val customTheme = CustomTheme("theme #${customThemes.size}", colors)
        customTheme.themeName.value = name

        customThemes.add(customTheme)
    }

    override fun getName(): String {
        return "Themes"
    }

    @Composable
    override fun display(ideStore: IdeStore) {
        ThemeView(ideStore.setting.theme)
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