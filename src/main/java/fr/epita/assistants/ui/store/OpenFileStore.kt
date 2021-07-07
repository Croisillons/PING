package fr.epita.assistants.ui.store

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.utils.CodeHighlight
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.dialog.Sed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Cursor

/**
 * Store an open file
 */
class OpenFileStore(val node: Node, val projectStore: ProjectStore) : EditorTab {
    /**
     * Name of the file
     */
    val filename: String = node.path.fileName.toString()

    /**
     * State of the content of the file
     */
    val content = mutableStateOf("")

    /**
     * State if the current file has unsaved changes
     */
    val hasChanged = mutableStateOf(false)
    val selected: Boolean
        get() {
            return projectStore.selectedEditorTab.value == this
        }

    /**
     * We load the file content when the file is opened
     */
    init {
        loadFileContent()
    }

    /**
     * Open and read file content in a Coroutine Scope
     */
    fun loadFileContent() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val result = node.content
            launch(Dispatchers.Main) {
                content.value = result
            }
        }
    }

    override fun getName(): String {
        return filename
    }

    @Composable
    override fun display(ideStore: IdeStore) {
        val sedState = remember { mutableStateOf(false) }
        val file = projectStore.selectedEditorTab.value!! as OpenFileStore

        val onValueChange: (it: String) -> Unit = {
            file.hasChanged.value = true
            file.content.value = it
        }

        val onReplace: (it: Boolean) -> Unit = {
            sedState.value = it
        }

        if (sedState.value) {
            Sed(file.content.value, onValueChange, onReplace)
        }

        val textStyle: TextStyle = TextStyle(
            MaterialTheme.colors.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight(600),
            fontFamily = FontFamily.Monospace,
            lineHeight = 22.sp
        )
        val horizontalScrollState = rememberScrollState()
        val verticalScrollState = rememberScrollState()
        SelectionContainer {
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondary, RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row {
                    Column(
                        modifier = Modifier.verticalScroll(verticalScrollState)
                    ) {
                        content.value.split("\n").forEachIndexed { idx, str ->
                            Text(
                                text = " $idx".padEnd(5) + " ",
                                style = textStyle
                            )
                        }
                    }
                    Divider(color = MaterialTheme.colors.onSecondary, modifier = Modifier.fillMaxHeight().width(0.5.dp))
                    BasicTextField(
                        value = content.value,
                        onValueChange = onValueChange,
                        textStyle = textStyle,
                        modifier = Modifier.padding(horizontal = 8.dp)
                            .fillMaxHeight()
                            .onPreviewKeyEvent {
                                val shortcuts = ideStore.setting.shortcuts
                                when {
                                    (shortcuts.save.isPressed(it)) -> {
                                        ideStore.project.value!!.saveFile()
                                        true
                                    }
                                    (shortcuts.replace.isPressed(it)) -> {
                                        onReplace(true)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            .horizontalScroll(horizontalScrollState)
                            .verticalScroll(verticalScrollState),
                        visualTransformation = CodeHighlight(MaterialTheme.colors, projectStore)
                    )

                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(verticalScrollState),
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                )
                HorizontalScrollbar(
                    adapter = rememberScrollbarAdapter(horizontalScrollState),
                    modifier = Modifier.padding(start = 58.dp, end = 12.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }

    @Composable
    override fun displayTab(ideStore: IdeStore, isSelected: Boolean, onClick: () -> Unit, onClose: () -> Unit) {
        val hoverState = remember { mutableStateOf(false) }
        Surface(
            color = if (isSelected) MaterialTheme.colors.secondary else if (hoverState.value) MaterialTheme.colors.onSurface else Color.Transparent,
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
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier.padding(8.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getName(),
                    color = MaterialTheme.colors.onSecondary
                )

                Icon(
                    if (!hoverState.value && hasChanged.value) Icons.Default.Circle else Icons.Default.Close,
                    tint = if (hoverState.value || hasChanged.value) MaterialTheme.colors.onSecondary else Color.Transparent,
                    contentDescription = "Close Tab",
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 4.dp)
                        .clickable(onClick = onClose)
                        .align(Alignment.CenterVertically)
                        .cursor(Cursor.HAND_CURSOR)
                )
            }
        }
    }

}