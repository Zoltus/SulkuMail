package fi.sulku.sulkumail.composables.screens.mail_editor.buttons.colorpicker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@Composable
fun ColorPickerBtn(
    showColorPicker: MutableState<Boolean>,
) {
    RichTextToolButton(
        onClick = { showColorPicker.value = !showColorPicker.value },
        isSelected = showColorPicker.value,
        icon = Icons.Filled.Colorize,
        contentDescription = ""
    )
}