package fr.epita.assistants.ui.view.menu

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.ui.model.IdeTheme
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.MenuBar

@Composable
fun IdeMenu(ideStore: IdeStore) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Box {
            Text(
                "Project",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.clickable { ideStore.menuBarState.value = MenuBar.PROJECT }
            )
            ProjectMenu(ideStore)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box {
            Text(
                "Theme",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.clickable { ideStore.menuBarState.value = MenuBar.THEME }
            )

            ThemeMenu(ideStore)
        }
        Spacer(modifier = Modifier.width(8.dp))

        if (ideStore.project.value?.project?.aspects?.any { it -> it.type == Mandatory.Aspects.MAVEN } == true) {
            Box {
                Text(
                    "Maven",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.clickable { ideStore.menuBarState.value = MenuBar.MAVEN }
                )
                MavenMenu(ideStore)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (ideStore.project.value?.project?.aspects?.any { it -> it.type == Mandatory.Aspects.GIT } == true) {
            Box {
                Text(
                    "Git",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.clickable { ideStore.menuBarState.value = MenuBar.GIT }
                )
                GitMenu(ideStore)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ProjectMenu(ideStore: IdeStore) {
    DropdownMenu(
        expanded = ideStore.menuBarState.value == MenuBar.PROJECT,
        onDismissRequest = { ideStore.menuBarState.value = null },
        modifier = Modifier.pointerMoveFilter(
            onExit = {
                ideStore.menuBarState.value = null
                false
            }
        )
            .background(MaterialTheme.colors.primary),
    ) {
        DropdownMenuItem(
            onClick = { ideStore.openProject() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Project", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.cleanProject() },
            enabled = ideStore.project.value != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clean Project", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.openProject() },
            enabled = ideStore.project.value != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export Project", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = { AppManager.exit() }
        ) {
            Text("Exit", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun ThemeMenu(ideStore: IdeStore) {
    DropdownMenu(
        expanded = ideStore.menuBarState.value == MenuBar.THEME,
        onDismissRequest = { ideStore.menuBarState.value = null },
        modifier = Modifier.pointerMoveFilter(
            onExit = {
                ideStore.menuBarState.value = null
                false
            }
        )
            .background(MaterialTheme.colors.primary),
    ) {
        DropdownMenuItem(
            onClick = { ideStore.setting.setTheme(IdeTheme.LIGHT) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Light", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.setTheme(IdeTheme.DARK) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.setTheme(IdeTheme.PINK) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pink", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.setTheme(IdeTheme.BLUE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Blue", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.setTheme(IdeTheme.GREEN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Green", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.openCustomTheme() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Custom Theme", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun MavenMenu(ideStore: IdeStore) {
    DropdownMenu(
        expanded = ideStore.menuBarState.value == MenuBar.MAVEN,
        onDismissRequest = { ideStore.menuBarState.value = null },
        modifier = Modifier.pointerMoveFilter(
            onExit = {
                ideStore.menuBarState.value = null
                false
            }
        )
            .background(MaterialTheme.colors.primary),
    ) {
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Install", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.compileProject() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Compile", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exec", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clean", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Package", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tree", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun GitMenu(ideStore: IdeStore) {
    DropdownMenu(
        expanded = ideStore.menuBarState.value == MenuBar.GIT,
        onDismissRequest = { ideStore.menuBarState.value = null },
        modifier = Modifier.pointerMoveFilter(
            onExit = {
                ideStore.menuBarState.value = null
                false
            }
        )
            .background(MaterialTheme.colors.primary),
    ) {
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pull", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Push", color = MaterialTheme.colors.onPrimary)
        }
    }
}
