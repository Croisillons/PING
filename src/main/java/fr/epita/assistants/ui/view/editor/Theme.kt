package fr.epita.assistants.ui.view.dialog

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epita.assistants.ui.store.ThemeStore
import fr.epita.assistants.ui.utils.cursor
import java.awt.Cursor


@Composable
fun ThemeView(themeStore: ThemeStore) {
    val onPrimary = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.onPrimary) }
    val primary = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.primary) }
    val onSecondary = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.onSecondary) }
    val secondary = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.secondary) }
    val background = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.background) }
    val onBackground = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.onBackground) }
    val onSurface = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.onSurface) }
    val primaryVariant = remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.primaryVariant) }
    val secondaryVariant =
        remember { mutableStateOf(themeStore.selectedCustomTheme.value.colors.secondaryVariant) }

    val (selectedColorTheme, setSelectedColorTheme) = remember { mutableStateOf(onPrimary) }

    val verticalScrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
                .verticalScroll(verticalScrollState)
                .padding(32.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomThemeList(themeStore) {
                    onPrimary.value = themeStore.selectedCustomTheme.value.colors.onPrimary
                    primary.value = themeStore.selectedCustomTheme.value.colors.primary
                    onSecondary.value = themeStore.selectedCustomTheme.value.colors.onSecondary
                    secondary.value = themeStore.selectedCustomTheme.value.colors.secondary
                    background.value = themeStore.selectedCustomTheme.value.colors.background
                    onBackground.value = themeStore.selectedCustomTheme.value.colors.onBackground
                    onSurface.value = themeStore.selectedCustomTheme.value.colors.onSurface
                    primaryVariant.value = themeStore.selectedCustomTheme.value.colors.primaryVariant
                    secondaryVariant.value = themeStore.selectedCustomTheme.value.colors.secondaryVariant
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Name",
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    TextField(
                        value = themeStore.selectedCustomTheme.value.themeName.value,
                        onValueChange = { themeStore.selectedCustomTheme.value.themeName.value = it },
                        singleLine = true,
                        textStyle = TextStyle(MaterialTheme.colors.onSecondary),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
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

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    themeStore.setCustomTheme(
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
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
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
        modifier = Modifier.fillMaxWidth(0.8f)
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
                text = "Hue",
                color = MaterialTheme.colors.onSecondary
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
                text = "Saturation",
                color = MaterialTheme.colors.onSecondary
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
                text = "Lightness",
                color = MaterialTheme.colors.onSecondary
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

@Composable
fun CustomThemeList(themeStore: ThemeStore, onClick: () -> Unit) {
    val horizontalScrollState = rememberScrollState(0)
    Row(
        modifier = Modifier.fillMaxWidth(0.7f),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(0.9f)) {
            Row(
                modifier = Modifier.horizontalScroll(horizontalScrollState)
            ) {
                themeStore.customThemes.forEach { theme ->
                    CustomThemeItem(
                        theme.themeName.value,
                        themeStore.selectedCustomTheme.value == theme,
                        { themeStore.selectCustomTheme(theme); onClick() }) { themeStore.removeCustomTheme(theme) }
                }
            }
            HorizontalScrollbar(
                modifier = Modifier
                    .padding(top = 4.dp),
                adapter = rememberScrollbarAdapter(horizontalScrollState)
            )
        }

        Column(
            modifier = Modifier.weight(0.1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier.background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                    .padding(2.dp)
                    .clickable { themeStore.addCustomTheme(); onClick() }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add theme",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }


    }

}

@Composable
fun CustomThemeItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val hoverState = remember { mutableStateOf(false) }
    Surface(
        color = if (hoverState.value or isSelected) MaterialTheme.colors.primary else Color.Transparent,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    hoverState.value = true
                    false
                },
                onExit = {
                    hoverState.value = false
                    false
                }
            )
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                color = if (hoverState.value or isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove Theme",
                tint = if (hoverState.value or isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(end = 4.dp)
                    .clickable { onRemove() }
            )
        }
    }
}