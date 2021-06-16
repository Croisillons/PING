package fr.epita.assistants.ui.store

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.imageFromResource
import kotlinx.coroutines.*

class SnackBarStore {
    val isVisible: MutableState<Boolean> = mutableStateOf(false)
    val title: MutableState<String> = mutableStateOf("")
    val successImage = imageFromResource("compilation-success.png")
    val failImage = imageFromResource("compilation-fail.png")
    val image: MutableState<ImageBitmap> = mutableStateOf(successImage)

    fun launchSnackBar() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            isVisible.value = true
            delay(2500L)
            isVisible.value = false
        }
    }
}