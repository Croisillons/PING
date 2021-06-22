package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.IdeStore
import fr.epita.assistants.ui.utils.IdeCard
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor


@Composable
fun CustomThemeCard(ideStore: IdeStore) {
    val onPrimaryColor = MaterialTheme.colors.onBackground
    val primaryColor = MaterialTheme.colors.primary
    val onSecondaryColor = MaterialTheme.colors.onSecondary
    val secondaryColor = MaterialTheme.colors.secondary
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val backgroundColor = MaterialTheme.colors.background
    val onSurfaceColor = MaterialTheme.colors.onSurface
    val primaryVariantColor = MaterialTheme.colors.primaryVariant
    val secondaryVariantColor = MaterialTheme.colors.secondaryVariant


    val onPrimary = remember { mutableStateOf(onPrimaryColor) }
    val primary = remember { mutableStateOf(primaryColor) }
    val onSecondary = remember { mutableStateOf(onSecondaryColor) }
    val secondary = remember { mutableStateOf(secondaryColor) }
    val background = remember { mutableStateOf(backgroundColor) }
    val onBackground = remember { mutableStateOf(onBackgroundColor) }
    val onSurface = remember { mutableStateOf(onSurfaceColor) }
    val primaryVariant = remember { mutableStateOf(primaryVariantColor) }
    val secondaryVariant = remember { mutableStateOf(secondaryVariantColor) }

    val (selectedColorTheme, setSelectedColorTheme) = remember { mutableStateOf(onPrimary) }


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
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ColorPickerItem("onPrimary", onPrimary, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("Primary", primary, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("onSecondary", onSecondary, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("Secondary", secondary, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("onBackground", onBackground, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("background", background, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("onSurface", onSurface, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("PrimaryVariant", primaryVariant, selectedColorTheme, setSelectedColorTheme)
                        ColorPickerItem("SecondaryVariant", secondaryVariant, selectedColorTheme, setSelectedColorTheme)
                    }

                    Box(modifier = Modifier.weight(0.5f)) {
                        ColorPicker(selectedColorTheme)
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
fun ColorPickerItem(
    title: String,
    color: MutableState<Color>,
    selectedColor: MutableState<Color>,
    onClick: (MutableState<Color>) -> Unit
) {
    val (hoverState, setHoverState) = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.7f)
            .clickable(onClick = { onClick(color) })
            .background(
                if (hoverState or (color == selectedColor)) MaterialTheme.colors.onSurface else Color.Transparent,
                RoundedCornerShape(4.dp)
            )
            .pointerMoveFilter(
                onEnter = {
                    setHoverState(true)
                    false
                },
                onExit = {
                    setHoverState(false)
                    false
                }
            )
            .cursor(Cursor.HAND_CURSOR),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier.width(28.dp)
                .height(12.dp)
                .padding(end = 16.dp)
        ) {
            Surface(
                color = color.value,
                modifier = Modifier.fillMaxSize()
            ) {}
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun ColorPicker(selectedColor: MutableState<Color>) {
    val hsb = java.awt.Color.RGBtoHSB(
        (selectedColor.value.red * 255f).toInt(),
        (selectedColor.value.green * 255f).toInt(),
        (selectedColor.value.blue * 255f).toInt(),
        null
    )

    val hueState = mutableStateOf(hsb[0])
    val saturationState = mutableStateOf(hsb[1])
    val lightnessState = mutableStateOf(hsb[2])

    val updateColor = {
        selectedColor.value =
            Color(java.awt.Color.HSBtoRGB(hueState.value, saturationState.value, lightnessState.value))
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