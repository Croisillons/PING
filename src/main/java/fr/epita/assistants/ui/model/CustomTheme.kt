package fr.epita.assistants.ui.model

import androidx.compose.material.Colors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Implements IdeTheme and represents a custom Theme
 */
class CustomTheme(
    private var title: String,
    override var colors: Colors,
    override var themeName: MutableState<String> = mutableStateOf(title)
) : IdeTheme {
}