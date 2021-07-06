package fr.epita.assistants.ui.utils

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.Dp
import fr.epita.assistants.myide.domain.service.MyProjectService
import fr.epita.assistants.ui.model.Shortcut
import fr.epita.assistants.ui.store.IdeStore
import java.io.FileInputStream
import java.io.IOException
import java.util.*

fun loadConfig(): IdeStore {
    val myProjectService: MyProjectService = MyProjectService()
    val ideStore: IdeStore = IdeStore(myProjectService)
    try {
        FileInputStream("./config.properties").use { input ->
            val prop = Properties()

            // load a properties file
            prop.load(input)

            // Load ide setting: project path + ide width and height
            prop.getProperty("project.path")?.let { path ->
                ideStore.loadProject(path)
                ideStore.project.value?.let { project ->
                    project.treeWidth.value = Dp(prop.getProperty("ide.width", "300").toFloat())
                    project.filesHeight.value = Dp(prop.getProperty("ide.height", "400").toFloat())
                }
            }

            // Load current theme
            ideStore.setting.theme.setCustomTheme(
                Color(Integer.parseInt(prop.getProperty("theme.onPrimary"))),
                Color(Integer.parseInt(prop.getProperty("theme.primary"))),
                Color(Integer.parseInt(prop.getProperty("theme.onSecondary"))),
                Color(Integer.parseInt(prop.getProperty("theme.secondary"))),
                Color(Integer.parseInt(prop.getProperty("theme.onBackground"))),
                Color(Integer.parseInt(prop.getProperty("theme.background"))),
                Color(Integer.parseInt(prop.getProperty("theme.onSurface"))),
                Color(Integer.parseInt(prop.getProperty("theme.primaryVariant"))),
                Color(Integer.parseInt(prop.getProperty("theme.secondaryVariant"))),
            )

            // Load custom themes
            var i = 0
            while (prop.getProperty("customTheme$i.onPrimary") != null) {
                val colors = lightColors(
                    onPrimary = Color(Integer.parseInt(prop.getProperty("customTheme$i.onPrimary"))),
                    primary = Color(Integer.parseInt(prop.getProperty("customTheme$i.primary"))),
                    onSecondary = Color(Integer.parseInt(prop.getProperty("customTheme$i.onSecondary"))),
                    secondary = Color(Integer.parseInt(prop.getProperty("customTheme$i.secondary"))),
                    onBackground = Color(Integer.parseInt(prop.getProperty("customTheme$i.onBackground"))),
                    background = Color(Integer.parseInt(prop.getProperty("customTheme$i.background"))),
                    onSurface = Color(Integer.parseInt(prop.getProperty("customTheme$i.onSurface"))),
                    primaryVariant = Color(Integer.parseInt(prop.getProperty("customTheme$i.primaryVariant"))),
                    secondaryVariant = Color(Integer.parseInt(prop.getProperty("customTheme$i.secondaryVariant"))),
                )
                val name = prop.getProperty("customTheme$i.name")
                ideStore.setting.theme.loadCustomTheme(name, colors)
                i++
            }

            if (ideStore.setting.theme.customThemes.size > 1)
                ideStore.setting.theme.customThemes.removeAt(0)

            // Load Shortcuts
            loadShortcut(prop, "save")?.let { ideStore.setting.shortcuts.save = it }
            loadShortcut(prop, "replace")?.let { ideStore.setting.shortcuts.replace = it }
            loadShortcut(prop, "jumpTo")?.let { ideStore.setting.shortcuts.jumpTo = it }
        }
    } catch (ex: IOException) {
        println("No configuration file")
    }
    return ideStore
}

fun loadShortcut(prop: Properties, name: String): Shortcut? {
    val shortcutString = prop.getProperty("shortcut.$name") ?: return null
    val ctrl = shortcutString.contains("Ctrl")
    val shift = shortcutString.contains("Shift")
    val alt = shortcutString.contains("Alt")
    val key = Key(shortcutString.last().code)

    val shortcut = Shortcut(ctrl, shift, alt, key)
    return shortcut
}
