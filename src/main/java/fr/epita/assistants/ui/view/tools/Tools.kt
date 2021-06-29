package fr.epita.assistants.ui.view.tools

import androidx.compose.desktop.SwingPanel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.RequestOrigin
import com.jediterm.terminal.TerminalColor
import com.jediterm.terminal.TerminalMode
import com.jediterm.terminal.TextStyle
import com.jediterm.terminal.emulator.ColorPalette
import com.jediterm.terminal.ui.JediTermWidget
import com.jediterm.terminal.ui.UIUtil
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider
import com.pty4j.PtyProcess
import java.awt.Dimension
import java.nio.charset.Charset
import javax.swing.BoxLayout
import javax.swing.JPanel

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
fun Tools() {
    val state = remember { TerminalState() }

    SideEffect {
        val settingsProvider = TerminalSettings();

        val tw = JediTermWidget(settingsProvider)

        tw.terminal.setModeEnabled(TerminalMode.AutoNewLine, true)

        lateinit var cmd : Array<String>;
        val env = mutableMapOf<String, String>();

        if (UIUtil.isWindows) {
            cmd = arrayOf("cmd.exe")
        } else {
            cmd = arrayOf("/bin/bash", "-l")
            env["TERM"] = "xterm"
        }

        val pty = PtyProcess.exec(cmd, env)

        tw.createTerminalSession(PtyProcessTtyConnector(pty, Charset.forName("UTF-8")))
        tw.start()

        state.widget = tw

        state.panel.layout = BoxLayout(state.panel, BoxLayout.Y_AXIS)
        state.panel.add(tw)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SwingPanel(
            background = Color.Black,
            factory = { state.panel },
        )
    }

}
