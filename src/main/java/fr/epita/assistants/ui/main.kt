package fr.epita.assistants.ui

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import fr.epita.assistants.ui.view.menu.IdeMenu

fun main() = Window(
    title = "IDE",
    menuBar = IdeMenu()
) {
    MaterialTheme {

    }
}