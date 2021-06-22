package fr.epita.assistants.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IdeCard(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(8.dp)),
            elevation = 32.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp),
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            Box(
                modifier = Modifier.padding(32.dp)
            ) {
                content()
            }
        }
    }
}