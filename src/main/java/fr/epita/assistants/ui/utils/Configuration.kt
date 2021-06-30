package fr.epita.assistants.ui.utils

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import fr.epita.assistants.myide.domain.service.MyProjectService
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

            // get the property value and print it out
            prop.getProperty("project.path")?.let { path ->
                ideStore.loadProject(path)
                ideStore.project.value?.let { project ->
                    project.treeWidth.value = Dp(prop.getProperty("ide.width", "300").toFloat())
                    project.filesHeight.value = Dp(prop.getProperty("ide.height", "400").toFloat())
                }
            }

            ideStore.setting.setCustomTheme(
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
                ideStore.setting.loadCustomTheme(colors)
                i++
            }

            if (ideStore.setting.customThemes.size > 1)
                ideStore.setting.customThemes.removeAt(0)
        }
    } catch (ex: IOException) {
        println("No configuration file")
    }
    return ideStore
}
