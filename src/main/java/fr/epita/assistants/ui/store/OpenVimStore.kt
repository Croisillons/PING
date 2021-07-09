package fr.epita.assistants.ui.store

import androidx.compose.desktop.SwingPanel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.UIUtil
import com.pty4j.PtyProcess
import fr.epita.assistants.myide.domain.entity.Node
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.utils.cursor
import fr.epita.assistants.ui.view.tools.TerminalSettings
import fr.epita.assistants.ui.view.tools.TerminalState
import kotlinx.coroutines.*
import java.awt.Cursor
import java.nio.charset.Charset
import javax.swing.BoxLayout
import kotlin.io.path.absolutePathString

class OpenVimStore(val node: Node, val projectStore: ProjectStore) : EditorTab {
    /**
     * Name of the file
     */
    val filename: String = node.path.fileName.toString()

    val state = TerminalState()

    val pty: PtyProcess

    /**
     * We create the vim process
     */
    init {
        val settingsProvider = TerminalSettings()

        val tw = JediTermWidget(settingsProvider)

        lateinit var cmd: Array<String>
        val env = HashMap(System.getenv())

        if (UIUtil.isWindows) {
            cmd = arrayOf("cmd.exe")
        } else {
            cmd = arrayOf("/usr/bin/vim", node.path.toString())
            env["TERM"] = "xterm"
        }

        pty = PtyProcess.exec(cmd, env, projectStore.project.rootNode.path.absolutePathString())

        val tab = this

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                pty.waitFor()
                projectStore.closeEditor(tab);
            }
        }

        tw.createTerminalSession(PtyProcessTtyConnector(pty, Charset.forName("UTF-8")))
        tw.start()

        state.widget = tw

        state.panel.layout = BoxLayout(state.panel, BoxLayout.Y_AXIS)
        state.panel.add(tw)
    }

    override fun dispose() {
        pty.destroy()
    }

    override fun getName(): String {
        return filename
    }

    @Composable
    override fun display(ideStore: IdeStore) {
        SelectionContainer {
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondary, RoundedCornerShape(12.dp))
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                SwingPanel(
                    factory = { state.panel },
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
                    Icons.Default.Close,
                    tint = if (hoverState.value) MaterialTheme.colors.onSecondary else Color.Transparent,
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
