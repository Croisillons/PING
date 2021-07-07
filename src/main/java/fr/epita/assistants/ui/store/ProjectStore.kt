package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.epita.assistants.features.any.RunDiagnosticsFeature
import fr.epita.assistants.features.any.RunFeature
import fr.epita.assistants.features.maven.PackageFeature
import fr.epita.assistants.myide.domain.entity.*
import fr.epita.assistants.ui.model.EditorTab
import fr.epita.assistants.ui.view.tools.BuildToolTab
import fr.epita.assistants.ui.view.tools.RunToolTab
import fr.epita.assistants.ui.view.tools.TerminalToolTab
import fr.epita.assistants.ui.view.tools.ToolTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.sound.sampled.AudioSystem
import kotlin.reflect.KClass
import java.io.IOException

import java.io.OutputStream
import javax.tools.Diagnostic
import javax.tools.JavaFileObject


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

    /**
     * Mutable list of the tool tabs
     */
    var toolsTabs: MutableList<ToolTab> = mutableListOf(BuildToolTab(), RunToolTab(), TerminalToolTab(this))

    /**
     * State of the selected tool tab
     */
    var selectedToolTab: MutableState<ToolTab> = mutableStateOf(toolsTabs[0])
    var diagnostics: MutableList<Diagnostic<out JavaFileObject>> = mutableListOf()

    /**
     * List of files of the project
     */
    val tree: MutableState<TreeStore> = mutableStateOf(TreeStore(this))

    /**
     * List of tab displaying a file content
     */
    val editorTabs: MutableList<EditorTab> = mutableStateListOf()

    /**
     * Currently viewed file tab
     */
    var selectedEditorTab: MutableState<EditorTab?> = mutableStateOf(null)

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
    fun incrementFilesHeight(x: Dp) {
        filesHeight.value = filesHeight.value.plus(x)

        // TODO: optimize it
        ideStore.saveConfig()
    }

    /**
     * Selects the given tooltab
     * @param toolTab: The tooltab to select
     */
    fun <T : ToolTab> selectToolTab(toolTab: KClass<T>) {
        val toSelect = toolsTabs.find { toolTab.isInstance(it) }
        if (toSelect != null)
            selectedToolTab.value = toSelect
    }

    /**
     * Compile Project done in background
     * Display if the compilation succeed or failed
     */
    fun compileProject() {
        compiling.value = true
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val result: Feature.ExecutionReport =
                ideStore.projectService.execute(
                    project,
                    Mandatory.Features.Maven.PACKAGE,
                    PackageFeature.Callback { output: InputStream ->
                        val buildTooltab = toolsTabs.find { BuildToolTab::class.isInstance(it) } as BuildToolTab

                        buildTooltab.state.widget.terminal.clearScreen()

                        val terminalOutputStream: OutputStream = object : OutputStream() {
                            @Throws(IOException::class)
                            override fun write(ch: Int) {
                                buildTooltab.je.processChar(ch.toChar(), buildTooltab.state.widget.terminal)
                            }
                        }

                        output.transferTo(terminalOutputStream)
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


    /**
     * Run the project only if there is a artefact
     * Done in background
     */
    fun run() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val result: Feature.ExecutionReport =
                ideStore.projectService.execute(
                    project,
                    Supplement.Features.Any.RUN,
                    RunFeature.Callback { streams: RunFeature.RunStreams ->
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

    /**
     * Stop the runtime by destroying the process
     */
    fun stop() {
        runStreams.value?.process?.destroy()
        running.value = false
    }

    /**
     * Get the compiled Artefact
     */
    fun getCompiledArtefact(): File? {
        val targetFolder = project.rootNode.children.find { it.isFolder and it.path.fileName.equals("target") }
        if (targetFolder != null) {
            val jarFile = targetFolder.children.find { it.isFile and it.path.fileName.endsWith(".jar") }
            return jarFile?.path?.toFile()
        }
        return null
    }

    fun runDiagnostics() {
        ideStore.projectService.execute(project, Supplement.Features.Any.RUN_DIAGNOSTICS, RunDiagnosticsFeature.Callback { diagnostics ->
            this.diagnostics = diagnostics
        })
    }

    /**
     * Make sound
     * @param file: The file sound to start
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
    fun selectEditorTab(openFile: EditorTab?) {
        selectedEditorTab.value = openFile
    }

    /**
     * Open a file to a tab and select it
     * @param node: node corresponding to the file
     */
    fun openFileEditor(node: Node) {
        var editor: EditorTab? = editorTabs.firstOrNull { it.getName() == node.path.fileName.toString() }

        if (editor == null) {
            editor = OpenFileStore(node, this)
            editorTabs.add(editor)
        }

        selectEditorTab(editor)
    }

    /**
     * Open theme editor tab
     */
    fun openThemeEditor(themeStore: ThemeStore) {
        if (!editorTabs.contains(themeStore)) {
            editorTabs.add(themeStore)
        }
        selectEditorTab(themeStore)
    }

    /**
     * Open shortcuts editor tab
     */
    fun openShortcutEditor(shortcutStore: ShortcutStore) {
        if (!editorTabs.contains(shortcutStore)) {
            editorTabs.add(shortcutStore)
        }
        selectEditorTab(shortcutStore)
    }

    /**
     * Close an open file tab
     * @param editorTab: file to close
     */
    fun closeEditor(editorTab: EditorTab) {
        val index = editorTabs.indexOf(editorTab)
        editorTabs.remove(editorTab)
        selectEditorTab(editorTabs.getOrNull(index.coerceAtMost(editorTabs.lastIndex)))
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
        val file = selectedEditorTab.value!! as OpenFileStore
        val node = file.node
        val content = file.content.value

        ideStore.projectService.nodeService.update(node, 0, Int.MAX_VALUE, content.toByteArray())

        file.hasChanged.value = false

        runDiagnostics()
    }

    /**
     * Git pull
     */
    fun gitPull() {
        executeFeature("Git Pull", Mandatory.Features.Git.PULL)
    }

    /**
     * Git push
     */
    fun gitPush() {
        executeFeature("Git Push", Mandatory.Features.Git.PUSH)
    }

    /**
     * Maven install
     */
    fun mavenInstall() {
        executeFeature("Maven Install", Mandatory.Features.Maven.INSTALL)
    }

    /**
     * Maven clean
     */
    fun mavenClean() {
        executeFeature("Maven Clean", Mandatory.Features.Maven.CLEAN)
    }

    /**
     * Maven exec
     */
    fun mavenExec() {
        executeFeature("Maven Exec", Mandatory.Features.Maven.EXEC)
    }

    /**
     * Maven test
     */
    fun mavenTest() {
        executeFeature("Maven Test", Mandatory.Features.Maven.TEST)
    }

    /**
     * Maven tree
     */
    fun mavenTree() {
        executeFeature("Maven Tree", Mandatory.Features.Maven.TREE)
    }

    /**
     * Execute Feature
     * @param name: name of the feature
     * @param feature: Type of the feature
     * @param callback: Callback after the feature is executed
     */
    fun executeFeature(name: String, feature: Feature.Type, callback: Any? = null) {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val result: Feature.ExecutionReport = ideStore.projectService.execute(project, feature, callback)
            launch(Dispatchers.Main) {
                if (result.isSuccess) {
                    snackBar.title.value = "$name succeed."
                    snackBar.image.value = snackBar.successImage
                    launch(Dispatchers.IO) {
                        makeSound(File("src/main/resources/yiha-success.wav").absoluteFile)
                    }
                } else {
                    snackBar.title.value = "$name failed."
                    snackBar.image.value = snackBar.failImage
                    launch(Dispatchers.IO) {
                        makeSound(File("src/main/resources/yiha-failed.wav").absoluteFile)
                    }
                }
                snackBar.launchSnackBar()
            }
        }
    }
}
