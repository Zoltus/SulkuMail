package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@Composable
fun ColorizeBg(
    state: RichTextState,
    selectedColor: Color
) {
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(SpanStyle(background = selectedColor)) },
        isSelected = state.currentSpanStyle.background == selectedColor,
        icon = Icons.Default.FormatColorFill,
        contentDescription = ""
    )
}