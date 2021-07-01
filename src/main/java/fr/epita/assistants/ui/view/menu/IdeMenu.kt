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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.ui.model.IdeThemeEnum
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.MenuBar
import fr.epita.assistants.ui.view.tools.TerminalToolTab

@Composable
fun IdeMenu(ideStore: IdeStore) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (ideStore.menuBarState.value == MenuBar.PROJECT) MaterialTheme.colors.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Project",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clickable { ideStore.menuBarState.value = MenuBar.PROJECT }
            )
            ProjectMenu(ideStore)
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (ideStore.menuBarState.value == MenuBar.THEME) MaterialTheme.colors.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Theme",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clickable { ideStore.menuBarState.value = MenuBar.THEME }
            )

            ThemeMenu(ideStore)
        }

        Text(
            "Shortcuts",
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp)
                .clickable { ideStore.setting.shortcuts.openShortcut() }
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (ideStore.menuBarState.value == MenuBar.TOOL) MaterialTheme.colors.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Tool",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clickable { ideStore.menuBarState.value = MenuBar.TOOL }
            )
            ToolMenu(ideStore)
        }

        if (ideStore.project.value?.project?.aspects?.any { it -> it.type == Mandatory.Aspects.MAVEN } == true) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(if (ideStore.menuBarState.value == MenuBar.MAVEN) MaterialTheme.colors.primary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Maven",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                        .clickable { ideStore.menuBarState.value = MenuBar.MAVEN }
                )
                MavenMenu(ideStore)
            }
        }

        if (ideStore.project.value?.project?.aspects?.any { it -> it.type == Mandatory.Aspects.GIT } == true) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(if (ideStore.menuBarState.value == MenuBar.GIT) MaterialTheme.colors.primary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Git",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                        .clickable { ideStore.menuBarState.value = MenuBar.GIT }
                )
                GitMenu(ideStore)
            }
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
            onClick = { ideStore.exportProject() },
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
            onClick = { ideStore.setting.theme.setTheme(IdeThemeEnum.LIGHT) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Light", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.theme.setTheme(IdeThemeEnum.DARK) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.theme.setTheme(IdeThemeEnum.PINK) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pink", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.theme.setTheme(IdeThemeEnum.BLUE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Blue", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.theme.setTheme(IdeThemeEnum.GREEN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Green", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.setting.theme.openCustomTheme() },
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
            onClick = { ideStore.project.value?.mavenInstall() },
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
            onClick = { ideStore.project.value?.mavenExec() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exec", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.mavenClean() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clean", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.compileProject() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Package", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.mavenTest() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.mavenTest() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tree", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun ToolMenu(ideStore: IdeStore) {
    DropdownMenu(
        expanded = ideStore.menuBarState.value == MenuBar.TOOL,
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
            onClick = {
                val newTerminal = TerminalToolTab(ideStore.project.value!!)
                ideStore.project.value?.toolsTabs?.add(newTerminal)
                ideStore.project.value?.selectedToolTab?.value = newTerminal
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("New terminal", color = MaterialTheme.colors.onPrimary)
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
            onClick = { ideStore.project.value?.gitPull() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pull", color = MaterialTheme.colors.onPrimary)
        }
        DropdownMenuItem(
            onClick = { ideStore.project.value?.gitPush() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Push", color = MaterialTheme.colors.onPrimary)
        }
    }
}
