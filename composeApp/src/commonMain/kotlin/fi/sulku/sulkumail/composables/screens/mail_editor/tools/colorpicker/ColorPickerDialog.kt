package fi.sulku.sulkumail.composables.screens.mail_editor.tools.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@ExperimentalComposeUiApi
@Composable
fun ColorPickerDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    onPickedColor: (Color) -> Unit,
    properties: DialogProperties = DialogProperties()
) {
    var showDialog by remember(show) { mutableStateOf(show) }
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest()
                showDialog = false
            }, properties = properties
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
            ) {
                ColorPicker(onColorChanged = { colorEnvelope ->
                    println("Color picked: ${colorEnvelope.color.toArgb()}")
                    onPickedColor(colorEnvelope.color)
                })
            }
        }
    }
}