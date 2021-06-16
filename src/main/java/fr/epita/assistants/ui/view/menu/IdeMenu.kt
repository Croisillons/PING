package fr.epita.assistants.ui.view.menu

import androidx.compose.desktop.AppManager
import androidx.compose.ui.window.v1.Menu
import androidx.compose.ui.window.v1.MenuBar
import androidx.compose.ui.window.v1.MenuItem
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.model.IdeTheme

fun IdeMenu(ideStore: IdeStore): MenuBar {
    return MenuBar(
        Menu(
            name = "Project",
            MenuItem(
                name = "Open Project",
                onClick = { ideStore.openProject() },
//                shortcut = KeyStroke(Key.I)
            ),
            MenuItem(
                name = "Clean Project",
                onClick = { ideStore.cleanProject() },
            ),
            MenuItem(
                name = "Export Project",
                onClick = { ideStore.exportProject() },
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
                onClick = { ideStore.setting.setTheme(IdeTheme.LIGHT) }
            ),
            MenuItem(
                name = "Dark",
                onClick = { ideStore.setting.setTheme(IdeTheme.DARK) }
            ),
            MenuItem(
                name = "Pink",
                onClick = { ideStore.setting.setTheme(IdeTheme.PINK) }
            ),
            MenuItem(
                name = "Blue",
                onClick = { ideStore.setting.setTheme(IdeTheme.BLUE) }
            ),
            MenuItem(
                name = "Green",
                onClick = { ideStore.setting.setTheme(IdeTheme.GREEN) }
            ),
        ),
        Menu(
            name = "Maven",
            MenuItem(
                name = "Install",
                onClick = {}
            ),
            MenuItem(
                name = "Compile",
                onClick = { ideStore.compileProject() }
            ),
            MenuItem(
                name = "Exec",
                onClick = {}
            ),
            MenuItem(
                name = "Clean",
                onClick = {}
            ),
            MenuItem(
                name = "Package",
                onClick = {}
            ),
            MenuItem(
                name = "Test",
                onClick = {}
            ),
            MenuItem(
                name = "Tree",
                onClick = {}
            ),
        ),
        Menu(
            name = "Git",
            MenuItem(
                name = "Pull",
                onClick = {}
            ),
            MenuItem(
                name = "Push",
                onClick = {}
            ),
        )
    )
}