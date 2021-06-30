package fr.epita.assistants.ui.view.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.epita.assistants.myide.domain.entity.Mandatory
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.tools.BuildToolTab
import fr.epita.assistants.ui.view.tools.RunToolTab
import java.awt.Cursor

/**
 * Display action buttons, depending on the aspects of the project
 * @param ideStore Instance of IdeStore
 */
@Composable
fun ActionsView(projectStore: ProjectStore) {
    if (projectStore.project.aspects.any { it.type == Mandatory.Aspects.MAVEN }) {
        Row {
            Icon(
                Icons.Default.Coffee,
                tint = if (projectStore.compiling.value) Color.DarkGray else MaterialTheme.colors.onSecondary,
                contentDescription = "Build Project",
                modifier = Modifier.padding(horizontal = 5.dp)
                    .clickable {
                        if (!projectStore.compiling.value) {
                            projectStore.selectToolTab(BuildToolTab::class)
                            projectStore.compileProject()
                        }
                    }
                    .cursor(Cursor.HAND_CURSOR)
            )
            Icon(
                Icons.Default.PlayArrow,
                tint = if (projectStore.running.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSecondary,
                contentDescription = "Run Project",
                modifier = Modifier.padding(horizontal = 5.dp)
                    .clickable {
                        if (!projectStore.running.value) {
                            projectStore.selectToolTab(RunToolTab::class)
                            projectStore.run()
                        }
                    }
                    .cursor(Cursor.HAND_CURSOR)
            )
            Icon(
                Icons.Default.Stop,
                tint = if (projectStore.running.value) Color(red = 199, green = 84, blue = 80, alpha = 255) else MaterialTheme.colors.onSecondary,
                contentDescription = "Stop Project",
                modifier = Modifier.padding(horizontal = 5.dp)
                    .clickable { projectStore.stop() }
                    .cursor(Cursor.HAND_CURSOR)
            )
        }
    }
}