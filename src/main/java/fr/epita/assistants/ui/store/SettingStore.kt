package fr.epita.assistants.ui.store

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

enum class IdeTheme(val colors: Colors) {
    LIGHT (
        lightColors(
            onPrimary = Color(0, 0, 0),
            primary = Color(255,255,255),
            secondary = Color(75,75,75),
            background = Color(255,255,255),
        )
    ),
    DARK(
        darkColors(
            onPrimary = Color(255, 255, 255),
            primary = Color(0,0,0),
            secondary = Color(75,75,75),
            background = Color(0,0,0),
        )
    )
}

class SettingStore {
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeTheme.DARK)

    fun setTheme(ideTheme: IdeTheme) {
        theme.value = ideTheme
    }
}