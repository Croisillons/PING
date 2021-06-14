package fr.epita.assistants.ui

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.epita.assistants.myide.domain.entity.MyProject
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.view.editor.EditorView
import fr.epita.assistants.ui.view.editor.OpenFilesView
import fr.epita.assistants.ui.view.menu.IdeMenu
import fr.epita.assistants.ui.view.tree.TreeView
import java.nio.file.Path

fun main() = Window(
    title = "IDE",
    menuBar = IdeMenu()
) {
    val myProjectService: MyProjectService = MyProjectService()
    val ideStore: IdeStore = IdeStore(myProjectService, Settings())
    ideStore.loadProject("./src/")

    MaterialTheme {
        IdeView(ideStore)
    }
}

@Composable
fun IdeView(ideStore: IdeStore) {
    Column {
        Row(modifier = Modifier.weight(0.7f)) {
            TreeView(ideStore.project)
            OpenFilesView(ideStore.project)
        }
        Row(modifier = Modifier.weight(0.3f)) {
//            Tools()
        }
    }
}