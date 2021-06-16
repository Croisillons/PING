package fr.epita.assistants.ui.view.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.store.IdeStore

@Composable
fun ActionsView(ideStore: IdeStore) {
    Icon(
        Icons.Default.Coffee,
        tint = MaterialTheme.colors.onSecondary,
        contentDescription = "Build Project",
        modifier = Modifier.padding(horizontal = 5.dp)
            .clickable { ideStore.compileProject() }
    )
    Icon(
        Icons.Default.PlayArrow,
        tint = MaterialTheme.colors.onSecondary,
        contentDescription = "Run Project",
        modifier = Modifier.padding(horizontal = 5.dp)
            .clickable { }
    )
    Icon(
        Icons.Default.Stop,
        tint = Color(red = 199, green = 84, blue = 80, alpha = 255),
        contentDescription = "Stop Project",
        modifier = Modifier.padding(horizontal = 5.dp)
            .clickable { }
    )
}