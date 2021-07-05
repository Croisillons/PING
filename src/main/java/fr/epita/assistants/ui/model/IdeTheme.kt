package fr.epita.assistants.ui.model

import androidx.compose.material.Colors
import androidx.compose.runtime.MutableState

interface IdeTheme {
    abstract var themeName: MutableState<String>
    abstract var colors: Colors
}
