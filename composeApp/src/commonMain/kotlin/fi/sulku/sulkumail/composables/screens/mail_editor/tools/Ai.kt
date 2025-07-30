package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton
import org.jetbrains.compose.resources.painterResource
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.robot

@Composable
fun Ai(
    state: RichTextState,
) {
    RichTextToolButton(
        onClick = {

        },
        painter = painterResource(Res.drawable.robot),
        contentDescription = ""
    )
}