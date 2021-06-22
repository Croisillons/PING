package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.IdeCard
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor


@Composable
fun CustomThemeCard(ideStore: IdeStore) {
    val onPrimary = remember { mutableStateOf(Color.Green) }
    val primary = remember { mutableStateOf(Color.Black) }
    val onSecondary = remember { mutableStateOf(Color.Black) }
    val secondary = remember { mutableStateOf(Color.Black) }
    val background = remember { mutableStateOf(Color.Black) }
    val onBackground = remember { mutableStateOf(Color.Black) }
    val onSurface = remember { mutableStateOf(Color.Black) }
    val primaryVariant = remember { mutableStateOf(Color.Black) }
    val secondaryVariant = remember { mutableStateOf(Color.Black) }

    val selectedItem: MutableState<MutableState<Color>> = remember { mutableStateOf(onPrimary) }

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
                        ColorPickerItem("onPrimary", onPrimary)
                        ColorPickerItem("Primary", primary)
                        ColorPickerItem("onSecondary", onSecondary)
                        ColorPickerItem("Secondary", secondary)
                        ColorPickerItem("onBackground", onBackground)
                        ColorPickerItem("background", background)
                        ColorPickerItem("onSurface", onSurface)
                        ColorPickerItem("PrimaryVariant", primaryVariant)
                        ColorPickerItem("SecondaryVariant", secondaryVariant)
                    }

                    Box(modifier = Modifier.weight(0.5f)) {
                        ColorPicker(selectedItem.value)
                    }
                }
            }

            Button(
                onClick = {
                    ideStore.setting.setCustomTheme(
                        onPrimary.value,
                        primary.value,
                        onSecondary.value,
                        secondary.value,
                        onBackground.value,
                        background.value,
                        onSurface.value,
                        primaryVariant.value,
                        secondaryVariant.value,
                    )
                },
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
fun ColorPickerItem(title: String, color: MutableState<Color>) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun ColorPicker(selectedColor: MutableState<Color>) {
    val hsb = java.awt.Color.RGBtoHSB(
        selectedColor.value.red.toInt(),
        selectedColor.value.green.toInt(),
        selectedColor.value.blue.toInt(),
        null
    )

    val hueState = remember { mutableStateOf(hsb[0]) }
    val saturationState = remember { mutableStateOf(hsb[1]) }
    val lightnessState = remember { mutableStateOf(1f - hsb[2]) }

    val updateColor = {
        selectedColor.value = Color(java.awt.Color.HSBtoRGB(hueState.value, saturationState.value, lightnessState.value))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = selectedColor.value,
            modifier = Modifier.width(64.dp).height(64.dp)
        ) {}
        Spacer(modifier = Modifier.height(32.dp))

        Column {
            Text(
                text = "Hue"
            )
            Slider(
                value = hueState.value,
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth(0.7f)
                    .padding(8.dp),
                onValueChange = { hue ->
                    hueState.value = hue
                    updateColor()
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(java.awt.Color.HSBtoRGB(hueState.value, 0.8f, 0.8f)),
                    activeTrackColor = Color(java.awt.Color.HSBtoRGB(hueState.value, 0.8f, 0.8f)),
                )
            )

            Text(
                text = "Saturation"
            )
            Slider(
                value = saturationState.value,
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth(0.7f)
                    .padding(8.dp),
                onValueChange = { hue ->
                    saturationState.value = hue
                    updateColor()
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(java.awt.Color.HSBtoRGB(hueState.value, saturationState.value, 0.8f)),
                    activeTrackColor = Color(java.awt.Color.HSBtoRGB(hueState.value, saturationState.value, 0.8f)),
                )
            )

            Text(
                text = "Lightness"
            )
            Slider(
                value = lightnessState.value,
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth(0.7f)
                    .padding(8.dp),
                onValueChange = { hue ->
                    lightnessState.value = hue
                    updateColor()
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(java.awt.Color.HSBtoRGB(hueState.value, 0.8f, lightnessState.value)),
                    activeTrackColor = Color(java.awt.Color.HSBtoRGB(hueState.value, 0.8f, lightnessState.value)),
                )
            )
        }

    }
}