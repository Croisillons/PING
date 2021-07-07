package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.imageFromResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Store for the compilation result popup
 */
class SnackBarStore {
    /**
     * State of the visibility of the snackbar
     */
    val isVisible: MutableState<Boolean> = mutableStateOf(false)

    /**
     * State of the title of the snackbar
     */
    val title: MutableState<String> = mutableStateOf("")

    /**
     * Default success image
     */
    val successImage = imageFromResource("compilation-success.png")

    /**
     * Default failed image
     */
    val failImage = imageFromResource("compilation-fail.png")

    /**
     * State of the image to display in the snackbar
     */
    val image: MutableState<ImageBitmap> = mutableStateOf(successImage)

    /**
     * Display the popup
     */
    fun launchSnackBar() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            isVisible.value = true
            delay(2500L)
            isVisible.value = false
        }
    }
}