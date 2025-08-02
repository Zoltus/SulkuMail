package fi.sulku.sulkumail.composables.screens.mail_editor.tools.colorpicker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPicker(
    activeColor: Color,
    onPickedColor: (Color) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    RichTextToolButton(
        onClick = { showDialog = !showDialog },
        isSelected = showDialog,
        icon = Icons.Filled.Colorize,
        tint = activeColor,
        contentDescription = ""
    )

    if (showDialog) {
        ColorPickerDialog(
            onDismissRequest = { showDialog = false },
            onPickedColor = onPickedColor
        )
    }
}