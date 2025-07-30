package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun LineThrought(
    state: RichTextState,
) {
    val overLine = SpanStyle(textDecoration = TextDecoration.LineThrough)
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(overLine) },
        isSelected = state.currentSpanStyle.textDecoration == TextDecoration.LineThrough,
        icon = Icons.Default.FormatStrikethrough,
        contentDescription = ""
    )
}