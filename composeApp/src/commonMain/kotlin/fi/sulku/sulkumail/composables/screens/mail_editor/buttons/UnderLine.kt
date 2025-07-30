package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun UnderLine(
    state: RichTextState,
) {
    val underLine = SpanStyle(textDecoration = TextDecoration.Underline)
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(underLine) },
        isSelected = state.currentSpanStyle.textDecoration == TextDecoration.Underline,
        icon = Icons.Default.FormatUnderlined,
        contentDescription = ""
    )
}