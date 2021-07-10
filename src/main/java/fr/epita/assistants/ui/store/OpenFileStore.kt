package fr.epita.assistants.ui.store

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.isTypedEvent
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.utils.CodeHighlight
import fr.epita.assistants.ui.utils.JumpTo
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.utils.searchInProject
import fr.epita.assistants.ui.view.dialog.Sed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Cursor
import java.awt.MouseInfo
import java.lang.Integer.max

/**
 * Store an open file
 */
class OpenFileStore(val node: Node, val projectStore: ProjectStore, private val offset: Int = 0) : EditorTab {
    /**
     * Name of the file
     */
    val filename: String = node.path.fileName.toString()

    /**
     * State of the content of the file
     */
    val content = mutableStateOf(TextFieldValue(""))

    /**
     * Initial content of the file after loading or after saving
     */
    val initialContent = mutableStateOf("")

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
                content.value = TextFieldValue(result)
                initialContent.value = result
            }
        }
    }

    override fun getName(): String {
        return filename
    }

    @Composable
    override fun display(ideStore: IdeStore) {
        val sedState = remember { mutableStateOf(false) }
        val searchState = remember { mutableStateOf(false) }
        val file = projectStore.selectedEditorTab.value!! as OpenFileStore

        val onSave: () -> Unit = {
            file.initialContent.value = file.content.value.text
            ideStore.project.value!!.saveFile()
        }

        val onValueChange: (it: TextFieldValue) -> Unit = {
            file.hasChanged.value = file.content.value.text != it.text
            val selection = if (it.text.contains("\t")) TextRange(it.selection.start, it.selection.end + 3) else TextRange(it.selection.start, it.selection.end)
            val textFieldValue = TextFieldValue(it.text.replace("\t", "    "), selection)
            file.content.value = textFieldValue

            if (file.hasChanged.value)
                ideStore.project.value!!.onEdit()
        }

        for (diagnostic in ideStore.project.value!!.diagnostics)
        {
            // Empty loop juste to use the diagnostic list, to force recomposition on change
        }

        val onReplace: (it: Boolean) -> Unit = {
            sedState.value = it
        }

        val onSearch: (it: Boolean) -> Unit = {
            searchState.value = it
        }

        val onJump: (it: TextFieldValue) -> Unit = {
            val cursor = it.selection.end
            val text = it.text
            var start = cursor
            var end = cursor
            while (start > 0 && text.substring(start - 1, end).matches(Regex("[a-zA-Z_]*"))) {
                start--
            }
            while (end < text.length && text.substring(start, end + 1).matches(Regex("[a-zA-Z_]*"))) {
                end++
            }
            searchInProject(projectStore, projectStore.project.rootNode.path.toString(), text.substring(start, end))
        }

        if (searchState.value) {
            JumpTo(onSearch) {
                searchInProject(projectStore, projectStore.project.rootNode.path.toString(), it)
            }
        }

        if (sedState.value) {
            Sed(file.content.value.text, onValueChange, onReplace)
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
        val scope = rememberCoroutineScope()

        if (offset != 0) {
            scope.launch { verticalScrollState.scrollTo((offset * textStyle.lineHeight.value).toInt())
            }
        }


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
                        content.value.text.split("\n").forEachIndexed { idx, str ->
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
                                if (it.key == Key.Backspace && it.type == KeyEventType.KeyDown) {
                                    val c1 = file.content.value.text[max(0, file.content.value.selection.end-1)]
                                    val c2 = file.content.value.text[file.content.value.selection.end]
                                    if (file.content.value.selection.end < file.content.value.text.length && ((c1 == '\'' || c1 == '"' && c1 == c2)) || (c1 == '(' && c2 == ')') || (c1 == '{' && c2 == '}')|| (c1 == '[' && c2 == ']')) {
                                        file.content.value = TextFieldValue(StringBuilder(file.content.value.text).deleteCharAt(file.content.value.selection.end-1).toString(), file.content.value.selection)
                                        file.content.value = TextFieldValue(StringBuilder(file.content.value.text).deleteCharAt(file.content.value.selection.end-1).toString(), TextRange(max(0,file.content.value.selection.start-1), max(0, file.content.value.selection.end -1)))
                                        return@onPreviewKeyEvent true
                                    } else
                                        return@onPreviewKeyEvent false
                                }
                                when {
                                    (shortcuts.save.isPressed(it)) -> {
                                        onSave()
                                        true
                                    }
                                    (shortcuts.replace.isPressed(it)) -> {
                                        onReplace(true)
                                        true
                                    }
                                    (shortcuts.jumpTo.isPressed(it)) -> {
                                        onJump(content.value)
                                        true
                                    }
                                    (shortcuts.search.isPressed(it)) -> {
                                        onSearch(true)
                                        true
                                    }
                                    else -> false
                                }
                            }
                                .onKeyEvent { event ->
                                    if (event.type == KeyEventType.KeyUp)
                                        return@onKeyEvent false
                                    when (event.nativeKeyEvent.keyChar) {
                                        '(' -> {
                                            file.content.value = TextFieldValue(StringBuilder(file.content.value.text).insert(file.content.value.selection.end, ')').toString(), file.content.value.selection)
                                            true
                                        }
                                        '{' -> {
                                            file.content.value = TextFieldValue(StringBuilder(file.content.value.text).insert(file.content.value.selection.end, '}').toString(), file.content.value.selection)
                                            true
                                        }
                                        '[' -> {
                                            file.content.value = TextFieldValue(StringBuilder(file.content.value.text).insert(file.content.value.selection.end, ']').toString(), file.content.value.selection)
                                            true
                                        }
                                        '"' -> {
                                            file.content.value = TextFieldValue(StringBuilder(file.content.value.text).insert(file.content.value.selection.end, '"').toString(), file.content.value.selection)
                                            true
                                        }
                                        '\'' -> {
                                            file.content.value = TextFieldValue(StringBuilder(file.content.value.text).insert(file.content.value.selection.end, '\'').toString(), file.content.value.selection)
                                            true
                                        }
                                        '\n' -> {
                                            val cursorPosition = file.content.value.selection.start
                                            val lines = file.content.value.text.subSequence(0, cursorPosition).split("\n")
                                            val line = lines[lines.lastIndex - 1]
                                            var spaceCount = 0
                                            while (spaceCount < line.count() && line[spaceCount] == ' ')
                                                spaceCount++
                                            val newText = StringBuilder(file.content.value.text).insert(file.content.value.selection.start, " ".repeat(spaceCount)).toString()
                                            file.content.value = TextFieldValue(newText, TextRange(file.content.value.selection.start + spaceCount))
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

    /*suspend fun scrollTo(verticalScrollState: ScrollState, textStyle: TextStyle) {
        verticalScrollState.scrollBy(offset * textStyle.lineHeight.value)
    }*/
}