package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.IdeCard
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor

@Composable
fun CustomThemeCard(ideStore: IdeStore) {
    val (onPrimary, setOnPrimary) = remember { mutableStateOf("") }
    val (primary, setPrimary) = remember { mutableStateOf("") }
    val (onSecondary, setOnSecondary) = remember { mutableStateOf("") }
    val (secondary, setSecondary) = remember { mutableStateOf("") }
    val (background, setBackground) = remember { mutableStateOf("") }
    val (onBackground, setOnBackground) = remember { mutableStateOf("") }
    val (onSurface, setOnSurface) = remember { mutableStateOf("") }
    val (primaryVariant, setPrimaryVariant) = remember { mutableStateOf("") }
    val (secondaryVariant, setSecondaryVariant) = remember { mutableStateOf("") }

    IdeCard {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.align(Alignment.End)
                        .clickable { ideStore.setting.dimissCustomTheme() }
                        .cursor(Cursor.HAND_CURSOR)
                )
                Text(
                    text = "Make your custom Theme",
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight(500)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    Column(
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        ColorPickerItem("onPrimary", setOnPrimary)
                        ColorPickerItem("Primary", setPrimary)
                        ColorPickerItem("onSecondary", setOnSecondary)
                        ColorPickerItem("Secondary", setSecondary)
                        ColorPickerItem("onBackground", setOnBackground)
                        ColorPickerItem("background", setBackground)
                        ColorPickerItem("onSurface", setOnSurface)
                        ColorPickerItem("PrimaryVariant", setPrimaryVariant)
                        ColorPickerItem("SecondaryVariant", setSecondaryVariant)
                    }
                }
            }

            Button(
                onClick = { ideStore.setting.setCustomTheme(
                    onPrimary,
                    primary,
                    onSecondary,
                    secondary,
                    onBackground,
                    background,
                    onSurface,
                    primaryVariant,
                    secondaryVariant,
                )},
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                modifier = Modifier.cursor(Cursor.HAND_CURSOR)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Set Theme",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }

    }
}

@Composable
fun ColorPickerItem(title: String, setColor: (String) -> Unit) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}