package fr.epita.assistants.ui.utils

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
        }
    } catch (ex: IOException) {
        println("No configuration file")
    }
    return ideStore
}
