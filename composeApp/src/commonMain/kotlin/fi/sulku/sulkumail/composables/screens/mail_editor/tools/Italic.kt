package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@Composable
fun Italic(
    state: RichTextState,
) {
    val italicStyle = SpanStyle(fontStyle = FontStyle.Italic)
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(italicStyle) },
        isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
        icon = Icons.Default.FormatItalic,
        contentDescription = ""
    )
}