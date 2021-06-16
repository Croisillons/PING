package fr.epita.assistants.ui.view.tools

import androidx.compose.desktop.SwingPanel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.TerminalMode
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import com.pty4j.PtyProcess
import java.nio.charset.Charset
import javax.swing.BoxLayout
import javax.swing.JPanel

class TerminalState {
    val panel = JPanel()
    lateinit var widget: JediTermWidget
}

@Composable
fun Tools() {
    val state = remember { TerminalState() }

    SideEffect {
        val settingsProvider = DefaultSettingsProvider();

        val tw = JediTermWidget(settingsProvider)

        tw.terminal.setModeEnabled(TerminalMode.AutoNewLine, true)

        val cmd = arrayOf("/bin/bash", "-l")
        val env = mapOf("TERM" to "xterm")

        val pty = PtyProcess.exec(cmd, env)

        tw.createTerminalSession(PtyProcessTtyConnector(pty, Charset.forName("UTF-8")))
        tw.start()

        state.widget = tw

        state.panel.layout = BoxLayout(state.panel, BoxLayout.Y_AXIS)
        state.panel.add(tw)
    }

    SwingPanel(
        background = Color.Black,
        modifier = Modifier.fillMaxSize(),
        factory = { state.panel }
    )
}
