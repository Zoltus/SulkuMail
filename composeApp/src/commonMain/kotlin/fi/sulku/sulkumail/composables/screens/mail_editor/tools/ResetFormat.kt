package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.runtime.Composable
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@OptIn(ExperimentalRichTextApi::class)
@Composable
fun ResetFormat(
    state: RichTextState,
) {
    RichTextToolButton(
        onClick = {
            //state.removeSpanStyle(SpanStyle(fontWeight = FontWeight.Bold), range)
        },
        icon = Icons.Default.FormatColorReset,
        contentDescription = ""
    )
}