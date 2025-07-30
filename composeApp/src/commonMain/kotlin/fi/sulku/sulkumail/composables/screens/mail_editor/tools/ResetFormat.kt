package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.runtime.Composable
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton

@Composable
fun ResetFormat(
    state: RichTextState,
) {
    RichTextToolButton(
        onClick = {
            //todo
        },
        icon = Icons.Default.FormatColorReset,
        contentDescription = ""
    )
}