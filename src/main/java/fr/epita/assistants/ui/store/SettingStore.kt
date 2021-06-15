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
            onPrimary = Color(87,90,94),
            primary = Color(218,220,221),
            onSecondary = Color(56, 63, 81),
            secondary = Color(235,235,235),
            background = Color(244,247,245),
            onSurface = Color(175,175,175,50)
        )
    ),
    DARK(
        darkColors(
            onPrimary = Color(255, 255, 255),
            primary = Color(40, 44, 52),
            onSecondary = Color(171,178,191),
            secondary =  Color(65,72,85),// Color(50,56,68),
            background = Color(33,37,43),
            onSurface = Color(175,175,175,50)
        )
    ),
    PINK(
        lightColors(
            onPrimary = Color(87,90,94),
            primary = Color(252,234,241),
            onSecondary = Color(56, 63, 81),
            secondary = Color(244,180,204),
            background = Color(255,235,235),
            onSurface = Color(254,93,159,50)
        )
    ),
    BLUE(
        lightColors(
            onPrimary = Color(255, 255, 255),
            primary = Color(44,62,80),
            onSecondary = Color(56, 63, 81),
            secondary = Color(238,238,238),
            background = Color(0, 122,204),
            onSurface = Color(3,85,102,50)
        )
    ),
    GREEN(
        darkColors(
            onPrimary = Color(200,200,200),
            primary = Color(10,73,85),
            onSecondary = Color(232,232,232),
            secondary = Color(0,179,151),
            background = Color(0, 43,55),
            onSurface = Color(87,90,94,50)
        )
    ),
}

class SettingStore {
    val theme: MutableState<IdeTheme> = mutableStateOf(IdeTheme.DARK)

    fun setTheme(ideTheme: IdeTheme) {
        theme.value = ideTheme
    }
}