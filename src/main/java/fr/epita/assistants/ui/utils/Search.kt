package fr.epita.assistants.ui.utils

import com.fasterxml.jackson.annotation.JsonRootName
import fr.epita.assistants.ui.store.ProjectStore
import fr.epita.assistants.ui.store.TreeStore
import org.jetbrains.skija.impl.Log
import java.io.File
import java.io.FileReader
import java.util.*

fun searchInFile(file : File, word : String): Int {

    val indexList = mutableListOf<Int>()
    val lines = file.readLines()

    for (i in lines.indices) {
        if (lines[i].matches(Regex("(public|protected|private|static|\\s)+[\\w\\s]+?$word\\(.*\\).*")))
            return i;
    }

    return -1
}

fun searchInProject(projectStore: ProjectStore, rootName: String, word : String) {
    File(rootName).walk().forEach {
        if (it.isFile) {
            var line = searchInFile(it, word)
            if (line != -1) {
                var currentNode = projectStore.project.rootNode
                while (currentNode.path != it.toPath()) {
                    currentNode = currentNode.children.find {
                        that -> it.absolutePath.startsWith(that.path.toString())
                    }
                }
                projectStore.openFileEditor(currentNode, line)
            }
        }
    }
}