package fr.epita.assistants.ui

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.store.SettingStore
import fr.epita.assistants.ui.view.editor.OpenFilesView
import fr.epita.assistants.ui.view.menu.IdeMenu
import fr.epita.assistants.ui.view.tree.TreeView

fun main() {
    val myProjectService: MyProjectService = MyProjectService()
    val ideStore: IdeStore = IdeStore(myProjectService, SettingStore())
    ideStore.loadProject("./src/")

    Window(
        title = "IDE",
        menuBar = IdeMenu(ideStore)
    ) {
        MaterialTheme(
            colors = ideStore.setting.theme.value.colors
        ) {
            IdeView(ideStore)
        }
    }
}

@Composable
fun IdeView(ideStore: IdeStore) {
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.weight(0.7f)) {
            TreeView(ideStore.project)
            OpenFilesView(ideStore.project)
        }
        Row(modifier = Modifier.weight(0.3f)) {
//            Tools()
        }
    }
}