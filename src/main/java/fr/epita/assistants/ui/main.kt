package fr.epita.assistants.ui

import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.store.SettingStore
import fr.epita.assistants.ui.store.SnackBarStore
import fr.epita.assistants.ui.view.editor.OpenFilesView
import fr.epita.assistants.ui.view.menu.IdeMenu
import fr.epita.assistants.ui.view.tree.TreeView

fun main() {
    val myProjectService: MyProjectService = MyProjectService()
    val ideStore: IdeStore = IdeStore(myProjectService, SettingStore())

    Window(
        title = "IDE",
        menuBar = IdeMenu(ideStore)
    ) {
        MaterialTheme(
            colors = ideStore.setting.theme.value.colors
        ) {
            if (ideStore.project.value != null) {
                IdeView(ideStore)
            } else {
                OpenProjectView { ideStore.openProject() }
            }
            SnackbarDemo(ideStore.snackBar)
        }
    }
}

@Composable
fun IdeView(ideStore: IdeStore) {
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.height(ideStore.project.value?.filesHeight!!.value)) {
            TreeView(ideStore.project.value!!)
            Box(
                Modifier.width(5.dp)
                    .fillMaxHeight()
                    .draggable(orientation = Orientation.Horizontal,
                        state = rememberDraggableState { ideStore.project.value!!.incrementTreeWidth(it.dp) })
            )
            OpenFilesView(ideStore.project.value!!)
        }
        Box(
            Modifier.height(5.dp)
                .fillMaxWidth()
                .draggable(orientation = Orientation.Vertical,
                    state = rememberDraggableState { ideStore.project.value!!.incrementFilesHeight(it.dp) })
        )
        Row(modifier = Modifier.fillMaxHeight()) {
//            Tools()
        }
    }
}

@Composable
fun OpenProjectView(onClick: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello World!",
            color = MaterialTheme.colors.onPrimary,
            fontSize = 42.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onClick, colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)) {
            Text(
                text = "Open a project",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun SnackbarDemo(snackBar: SnackBarStore) {
    Column {
        if (snackBar.isVisible.value) {
            Snackbar(
                action = {
                    Button(
                        onClick = {snackBar.isVisible.value = false}
                    ) {
                        Text("Close")
                    }
                },
                modifier = Modifier.padding(8.dp)
                    .height(100.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = snackBar.image.value,
                        contentDescription = "image",
                        modifier = Modifier.height(80.dp)
                    )
                    Text(
                        text = snackBar.title.value,
                        fontWeight = FontWeight(600),
                    )
                }
            }
        }
    }
}