package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun ColorizeText(
    state: RichTextState,
    selectedColor : Color
) {
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(SpanStyle(color = selectedColor)) },
        isSelected = state.currentSpanStyle.color == selectedColor,
        icon = Icons.Default.FormatColorText,
        tint = selectedColor,
        contentDescription = ""
    )
}