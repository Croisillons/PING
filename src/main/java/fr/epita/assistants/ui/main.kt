package fr.epita.assistants.ui

import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.store.SettingStore
import fr.epita.assistants.ui.view.actions.ActionsView
import fr.epita.assistants.ui.store.SnackBarStore
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.editor.OpenFilesView
import fr.epita.assistants.ui.view.menu.IdeMenu
import fr.epita.assistants.ui.view.tree.TreeView
import java.awt.Cursor

fun main() {
    val myProjectService: MyProjectService = MyProjectService()
    val ideStore: IdeStore = IdeStore(myProjectService, SettingStore())

    Window(
        title = "IDE",
        menuBar = IdeMenu(ideStore),
        size = IntSize(1400, 800),
        centered = true
    ) {
        MaterialTheme(
            colors = ideStore.setting.theme.value.colors
        ) {
            if (ideStore.project.value != null) {
                ProjectView(ideStore.project.value!!)
                SnackbarView(ideStore.project.value!!.snackBar)
            } else {
                OpenProjectView { ideStore.openProject() }
            }
        }
    }
}

/**
 *
 */
@Composable
fun ProjectView(projectStore: ProjectStore) {
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Row(
                modifier = Modifier.height(Dp(35f))
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
        ) {
            ActionsView(projectStore)
        }
        Row(modifier = Modifier.height(projectStore.filesHeight.value)) {
            TreeView(projectStore)
            Row(
                Modifier.width(12.dp)
                    .fillMaxHeight()
                    .draggable(orientation = Orientation.Horizontal,
                        state = rememberDraggableState { projectStore.incrementTreeWidth(it.dp) })
                    .cursor(Cursor.E_RESIZE_CURSOR),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DragIndicator,
                    contentDescription = "Resize View",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            OpenFilesView(projectStore)
        }
        Column(
            Modifier.height(12.dp)
                .fillMaxWidth()
                .draggable(orientation = Orientation.Vertical,
                    state = rememberDraggableState { projectStore.incrementFilesHeight(it.dp) })
                .cursor(Cursor.N_RESIZE_CURSOR),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.DragHandle,
                contentDescription = "Resize View",
                tint = MaterialTheme.colors.onPrimary
            )
        }
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
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
            modifier = Modifier.cursor(Cursor.HAND_CURSOR)
        ) {
            Text(
                text = "Open a project",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun SnackbarView(snackBar: SnackBarStore) {
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