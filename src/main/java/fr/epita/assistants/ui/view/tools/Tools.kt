package fr.epita.assistants.ui.view.tools

import androidx.compose.desktop.SwingPanel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.ArrayTerminalDataStream
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TerminalMode
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.emulator.ColorPalette
import com.jediterm.terminal.emulator.JediEmulator
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.UIUtil
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import com.pty4j.PtyProcess
import fr.epita.assistants.ui.store.ProjectStore
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset
import javax.swing.BoxLayout
import javax.swing.JPanel
import kotlin.io.path.absolutePathString


class TerminalState {
    val panel = JPanel()
    lateinit var widget: JediTermWidget
}

class Palette(private val myColors: Array<java.awt.Color>) : ColorPalette() {
    override fun getForegroundByColorIndex(colorIndex: Int): java.awt.Color {
        return myColors[colorIndex]
    }

    override fun getBackgroundByColorIndex(colorIndex: Int): java.awt.Color {
        return myColors[colorIndex]
    }
}

class TerminalSettings : DefaultSettingsProvider() {
    private val palette = Palette(
        arrayOf(
            java.awt.Color(0x3f4451),  //Black
            java.awt.Color(0xe05561),  //Red
            java.awt.Color(0x8cc265),  //Green
            java.awt.Color(0xd18f52),  //Yellow
            java.awt.Color(0x4aa5f0),  //Blue
            java.awt.Color(0xc162de),  //Magenta
            java.awt.Color(0x42b3c2),  //Cyan
            java.awt.Color(0xe6e6e6),  //White
            //Bright versions of the ISO colors
            java.awt.Color(0x4f5666),  //Black
            java.awt.Color(0xff616e),  //Red
            java.awt.Color(0xa5e075),  //Green
            java.awt.Color(0xf0a45d),  //Yellow
            java.awt.Color(0x4dc4ff),  //Blue
            java.awt.Color(0xde73ff),  //Magenta
            java.awt.Color(0x4cd1e0),  //Cyan
            java.awt.Color(0xd7dae0),  //White
        )
    );

    override fun getDefaultStyle(): TextStyle {
        return TextStyle(TerminalColor.WHITE, TerminalColor.BLACK);
    }

    override fun getTerminalColorPalette(): ColorPalette {
        return palette;
    }
}

@Composable
fun TerminalWindow(state: TerminalState) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SwingPanel(
            background = Color.Black,
            factory = { state.panel },
        )
    }

}

/**
 * Display the Tool section
 */
@Composable
fun Tools(projectStore: ProjectStore)
{
    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        ToolTabs(projectStore)

        projectStore.selectedToolTab.value.display(projectStore)
    }
}

/**
 * Display all tool tabs
 */
@Composable
fun ToolTabs(projectStore: ProjectStore) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .padding(horizontal = 8.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (tab in projectStore.toolsTabs) {
            tab.displayTab(projectStore, projectStore.selectedToolTab.value == tab)
        }
    }
}

/**
 * Represents a tool, containing the tab and the window
 */
interface ToolTab
{
    /**
     * Name displayed on the tab
     */
    fun getName(): String

    /**
     * Display the window when selected
     */
    @Composable
    fun display(projectStore: ProjectStore)

    /**
     * Display the tab button
     */
    @Composable
    fun displayTab(projectStore: ProjectStore, isSelected: Boolean)
    {
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
                .clickable(onClick = { projectStore.selectedToolTab.value = this })
        ) {
            Row(
                modifier = Modifier.padding(8.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getName(),
                    color = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

class BuildToolTab : ToolTab
{
    val state = TerminalState()

    var je : JediEmulator;

    init {
        val settingsProvider = TerminalSettings()

        val tw = JediTermWidget(settingsProvider)

        tw.terminal.setModeEnabled(TerminalMode.AutoNewLine, true)
        je = JediEmulator(ArrayTerminalDataStream(charArrayOf()), tw.terminal)

        tw.start()

        state.widget = tw

        state.panel.layout = BoxLayout(state.panel, BoxLayout.Y_AXIS)
        state.panel.add(tw)
    }

    override fun getName(): String {
        return "Build"
    }

    @Composable
    override fun display(projectStore: ProjectStore) {
        TerminalWindow(state)
    }
}

class TerminalToolTab(val projectStore: ProjectStore) : ToolTab
{
    val state = TerminalState()

    init {
        val settingsProvider = TerminalSettings()

        val tw = JediTermWidget(settingsProvider)

        lateinit var cmd : Array<String>
        val env = HashMap(System.getenv())

        if (UIUtil.isWindows) {
            cmd = arrayOf("cmd.exe")
        } else {
            cmd = arrayOf(env.getOrDefault("SHELL", "/bin/bash"), "-l")
            env["TERM"] = "xterm"
        }

        val pty = PtyProcess.exec(cmd, env, projectStore.project.rootNode.path.absolutePathString())

        tw.createTerminalSession(PtyProcessTtyConnector(pty, Charset.forName("UTF-8")))
        tw.start()

        state.widget = tw

        state.panel.layout = BoxLayout(state.panel, BoxLayout.Y_AXIS)
        state.panel.add(tw)
    }

    override fun getName(): String {
        return "Terminal"
    }

    @Composable
    override fun display(projectStore: ProjectStore) {
        TerminalWindow(state)
    }

}

class RunToolTab : ToolTab {
    override fun getName(): String {
        return "Run"
    }

    @Composable
    override fun display(projectStore: ProjectStore) {
        SelectionContainer {
            Surface(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val scroll = rememberScrollState(0)
                Text(
                    text = projectStore.runOutputText.value,
                    modifier = Modifier
                        .verticalScroll(scroll)
                        .padding(8.dp)
                )
            }
        }
    }

}
