package fr.epita.assistants.ui.model

import androidx.compose.runtime.Composable
import fr.epita.assistants.ui.store.IdeStore

interface EditorTab {
    fun getName(): String

    @Composable
    fun display(ideStore: IdeStore)

    @Composable
    fun displayTab(ideStore: IdeStore, isSelected: Boolean, onClick: () -> Unit, onClose: () -> Unit)
}