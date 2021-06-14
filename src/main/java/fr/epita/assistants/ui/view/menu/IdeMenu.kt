package fr.epita.assistants.ui.view.menu

import androidx.compose.desktop.AppManager
import androidx.compose.ui.window.v1.Menu
import androidx.compose.ui.window.v1.MenuBar
import androidx.compose.ui.window.v1.MenuItem

fun IdeMenu() : MenuBar {
    return MenuBar(
        Menu(
            name = "File",
            MenuItem(
                name = "Open Project",
                onClick = {  },
//                shortcut = KeyStroke(Key.I)
            ),
            MenuItem(
                name = "Exit",
                onClick = { AppManager.exit() },
//                shortcut = KeyStroke(Key.X)
            )
        ),
        Menu(
            name = "Theme",
            MenuItem(
                name = "Light",
                onClick = {  }
            ),
            MenuItem(
                name = "Dark",
                onClick = {  }
            ),
            MenuItem(
                name = "Pink",
                onClick = {  }
            ),
            MenuItem(
                name = "Blue",
                onClick = {  }
            ),
            MenuItem(
                name = "Green",
                onClick = {  }
            ),
        )
    )
}