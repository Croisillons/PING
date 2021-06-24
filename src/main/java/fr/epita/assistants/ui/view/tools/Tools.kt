package fr.epita.assistants.ui.view.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import fr.epita.assistants.ui.store.ProjectStore

@Composable
fun Tools(projectStore: ProjectStore)
{
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        ToolTabs(projectStore)
        BuildWindow(projectStore) // FIXME
    }
}

@Composable
fun ToolTabs(projectStore: ProjectStore) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BuildTab(projectStore)
    }
}

@Composable
fun BuildTab(projectStore: ProjectStore)
{
    val hoverState = remember { mutableStateOf(false) }
    Surface(
        color = if (hoverState.value) MaterialTheme.colors.onSurface else Color.Transparent,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    hoverState.value = true
                    false
                },
                onExit = {
                    hoverState.value = false
                    false
                }
            )
            .padding(end = 4.dp)
            //.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Build",
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
fun BuildWindow(projectStore: ProjectStore) {
    SelectionContainer {
        Surface(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            if (projectStore.compilationOutput.value == null)
                return@Surface
            val byteArr = projectStore.compilationOutput.value!!.readAllBytes()
            val outputStr = String(byteArr)
            val scroll = rememberScrollState(0)
            Text(
                text = outputStr,
                modifier = Modifier
                    .verticalScroll(scroll)
            )
        }
    }
}