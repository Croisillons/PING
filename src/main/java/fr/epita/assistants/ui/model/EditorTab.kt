package fr.epita.assistants.ui.model

import androidx.compose.runtime.Composable
import fr.epita.assistants.ui.store.IdeStore

/**
 * Interface for all editor's tabs
 */
interface EditorTab {
    /**
     * Returns the name of the tab
     */
    fun getName(): String

    /**
     * Returns the composable of the content
     */
    @Composable
    fun display(ideStore: IdeStore)

    /**
     * Returns the composable of the tab
     */
    @Composable
    fun displayTab(ideStore: IdeStore, isSelected: Boolean, onClick: () -> Unit, onClose: () -> Unit)
}