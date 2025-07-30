package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun Bold(
    state: RichTextState,
) {
    val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)
    RichTextToolButton(
        onClick = { state.toggleSpanStyle(boldStyle) },
        isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
        icon = Icons.Outlined.FormatBold,
        contentDescription = ""
    )
}