package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import fi.sulku.sulkumail.composables.screens.mail_editor.RichTextToolButton
import org.jetbrains.compose.resources.painterResource
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.robot

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Ai(
    onTogglePanel: () -> Unit,
    isAiOpen: Boolean
) {
    RichTextToolButton(
        onClick = onTogglePanel,
        isSelected = isAiOpen,
        painter = painterResource(Res.drawable.robot),
        contentDescription = ""
    )
}