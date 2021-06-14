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
            onPrimary = Color(56, 63, 81),
            primary = Color(244,247,245),
            onSecondary = Color(87,90,94),
            secondary = Color(244,247,245),
            background = Color(244,247,245),
            onSurface = Color(175,175,175,50)
        )
    ),
    DARK(
        darkColors(
            onPrimary = Color(255, 255, 255),
            primary = Color(33,37,43),
            onSecondary = Color(171,178,191),
            secondary = Color(40, 44, 52), // Color(50,56,68),
            background = Color(33,37,43),
            onSurface = Color(175,175,175,50)
        )
    ),
    PINK(
        lightColors(
            onPrimary = Color(241, 228, 243),
            primary = Color(254,93,159),
            onSecondary = Color(244,187,211),
            secondary = Color(246,134,189),
            background = Color(254,93,159),
            onSurface = Color(254,93,159,50)
        )
    ),
    BLUE(
        lightColors(
            onPrimary = Color(242,242, 242),
            primary = Color(3,33, 102),
            onSecondary = Color(217,217,217),
            secondary = Color(6,47,79),
            background = Color(3,33, 102),
            onSurface = Color(3,85,102,50)
        )
    ),
    GREEN(
        lightColors(
            onPrimary = Color(232,232,232),
            primary = Color(35,128,130),
            onSecondary = Color(200,200,200),
            secondary = Color(3,85,102),
            background = Color(35,128,130),
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