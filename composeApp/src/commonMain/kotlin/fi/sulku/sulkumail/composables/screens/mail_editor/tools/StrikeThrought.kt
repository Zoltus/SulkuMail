package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@Composable
fun StrikeThrought(
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