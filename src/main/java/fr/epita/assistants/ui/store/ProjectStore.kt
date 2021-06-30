package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.epita.assistants.features.any.RunFeature
import fr.epita.assistants.features.maven.PackageFeature
import fr.epita.assistants.myide.domain.entity.*
import fr.epita.assistants.ui.view.tools.BuildToolTab
import fr.epita.assistants.ui.view.tools.RunToolTab
import fr.epita.assistants.ui.view.tools.TerminalToolTab
import fr.epita.assistants.ui.view.tools.ToolTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.security.cert.Extension
import javax.sound.sampled.AudioSystem
import kotlin.reflect.KClass

/**
 * Class used to store all the state of the project.
 *
 * @property ideStore stores the IDE loading this project
 * @property project stores the current project and its files
 */
class ProjectStore(val ideStore: IdeStore, val project: Project) {
    var compiling = mutableStateOf(false)
    var compilationOutput: MutableState<InputStream?> = mutableStateOf(null)
    var compilationOutputText: MutableState<String> = mutableStateOf("")
    var running = mutableStateOf(false)
    var runOutputText: MutableState<String> = mutableStateOf("")
    var runStreams: MutableState<RunFeature.RunStreams?> = mutableStateOf(null)
    val snackBar: SnackBarStore = SnackBarStore()
    var toolsTabs: MutableList<ToolTab> = mutableListOf(BuildToolTab(), RunToolTab(), TerminalToolTab(this))
    var selectedToolTab: MutableState<ToolTab> = mutableStateOf(toolsTabs[0])

    /**
     * List of files of the project
     */
    val tree: MutableState<TreeStore> = mutableStateOf(TreeStore(this))

    /**
     * List of tab displaying a file content
     */
    val openFiles: MutableList<OpenFileStore> = mutableStateListOf()

    /**
     * Currently viewed file tab
     */
    var selectedOpenFile: MutableState<OpenFileStore?> = mutableStateOf(null)

    /**
     * Resize the views
     */
    val treeWidth: MutableState<Dp> = mutableStateOf(300.dp)
    val filesHeight: MutableState<Dp> = mutableStateOf(550.dp)

    /**
     * Increments the width of the file tree composable
     * @param x the value to add to treeWidth
     */
    fun incrementTreeWidth(x: Dp) {
        treeWidth.value = treeWidth.value.plus(x)

        // TODO: optimize it
        ideStore.saveConfig()
    }

    /**
     * Increments the height of the file tree and editor composables
     * @param x the value to add to filesHeight
     */
    fun incrementFilesHeight(x: Dp)
    {
        filesHeight.value = filesHeight.value.plus(x)

        // TODO: optimize it
        ideStore.saveConfig()
    }

    fun <T: ToolTab> selectToolTab(toolTab: KClass<T>)
    {
        val toSelect = toolsTabs.find { toolTab.isInstance(it) }
        if (toSelect != null)
            selectedToolTab.value = toSelect
    }

    /**
     * Compile Project
     */
    fun compileProject() {
        compiling.value = true
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val result: Feature.ExecutionReport =
                    ideStore.projectService.execute(project, Mandatory.Features.Maven.PACKAGE, PackageFeature.Callback { output: InputStream ->
                        compilationOutput.value = output
                        launch(Dispatchers.IO) {

                            var res = ""
                            while (true) {
                                val byteToRead = output.available()
                                if (byteToRead == 0)
                                    continue
                                val bytes = output.readNBytes(byteToRead)
                                if (bytes.count() == 0)
                                    break
                                res += String(bytes)

                                launch(Dispatchers.Main) {
                                    compilationOutputText.value = res
                                }
                            }
                        }
                    })
            launch(Dispatchers.Main) {
                compiling.value = false
                if (result.isSuccess) {
                    // Display happy cowboy and Hiyaa
                    snackBar.title.value = "Compilation succeed."
                    snackBar.image.value = snackBar.successImage
                    launch(Dispatchers.IO) {
                        makeSound(File("src/main/resources/yiha-success.wav").absoluteFile)
                    }
                } else {
                    // Display sad cowboy and Hiyaa
                    snackBar.title.value = "Compilation failed."
                    snackBar.image.value = snackBar.failImage
                    launch(Dispatchers.IO) {
                        makeSound(File("src/main/resources/yiha-success.wav").absoluteFile)
                    }
                }
                snackBar.launchSnackBar()
            }
        }
    }

    fun run() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val result: Feature.ExecutionReport =
                ideStore.projectService.execute(project, Supplement.Features.Any.RUN, RunFeature.Callback { streams: RunFeature.RunStreams ->
                    launch(Dispatchers.IO) {
                        var res = ""
                        running.value = true
                        runStreams.value = streams
                        while (running.value) {
                            res += streams.readOutput()
                            res += streams.readError()

                            launch(Dispatchers.Main) {
                                runOutputText.value = res
                            }
                        }
                    }
                })
            running.value = false
        }
    }

    fun stop() {
        runStreams.value?.process?.destroy()
        running.value = false
    }

    fun getCompiledArtefact() : File? {
        val targetFolder = project.rootNode.children.find { it.isFolder and it.path.fileName.equals("target") }
        if (targetFolder != null) {
            val jarFile = targetFolder.children.find { it.isFile and it.path.fileName.endsWith(".jar") }
            return jarFile?.path?.toFile()
        }
        return null
    }

    /**
     * Make sound
     */
    fun makeSound(file: File) {
        val audioInputStream = AudioSystem.getAudioInputStream(file)
        val clip = AudioSystem.getClip()
        clip.open(audioInputStream)
        clip.start()
    }

    /**
     * If true, expands the dropDownMenu of file actions
     */
    val showFileActions: MutableState<Boolean> = mutableStateOf(false)


    /**
     * Select an open file tab
     * @param openFile: file to open
     */
    fun selectOpenFile(openFile: OpenFileStore?) {
        selectedOpenFile.value = openFile
    }

    /**
     * Open a file to a tab and select it
     * @param node: node corresponding to the file
     */
    fun openFileEditor(node: Node) {
        var editor: OpenFileStore? = openFiles.firstOrNull { it.node == node }

        if (editor == null) {
            editor = OpenFileStore(node, this)
            openFiles.add(editor)
        }

        selectOpenFile(editor)
    }

    /**
     * Close an open file tab
     * @param openFile: file to close
     */
    fun closeEditor(openFile: OpenFileStore) {
        val index = openFiles.indexOf(openFile)
        openFiles.remove(openFile)
        selectOpenFile(openFiles.getOrNull(index.coerceAtMost(openFiles.lastIndex)))
    }

    /**
     * Get Project Name Truncated to 20 char
     */
    fun getTruncatedProjectName(): String {
        var projectName = project.rootNode.path.fileName.toString()
        if (projectName.length > 20)
            projectName = projectName.take(20) + "..."
        return projectName
    }

    /**
     * Save selected file
     */
    fun saveFile() {
        val node = selectedOpenFile.value!!.node
        val content = selectedOpenFile.value!!.content.value

        ideStore.projectService.nodeService.update(node, 0, Int.MAX_VALUE, content.toByteArray())

        selectedOpenFile.value!!.hasChanged.value = false
    }
}
