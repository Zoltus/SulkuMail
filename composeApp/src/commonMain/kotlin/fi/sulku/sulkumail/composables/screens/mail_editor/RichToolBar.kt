package fi.sulku.sulkumail.composables.screens.mail_editor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import fi.sulku.sulkumail.composables.screens.mail_editor.buttons.Bold
import fi.sulku.sulkumail.composables.screens.mail_editor.buttons.ColorizeBg
import fi.sulku.sulkumail.composables.screens.mail_editor.buttons.ColorizeText
import fi.sulku.sulkumail.composables.screens.mail_editor.buttons.colorpicker.ColorPickerBtn
import fi.sulku.sulkumail.composables.screens.mail_editor.buttons.colorpicker.ColorPickerDialog
import fi.sulku.sulkumail.theme.CustomColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RichTextHeaderToolBar(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    var activeColor by remember { mutableStateOf(CustomColor.discordBlue) }
    val showColorPicker = remember { mutableStateOf(false) }


    ColorPickerDialog(
        show = showColorPicker.value,
        onDismissRequest = { showColorPicker.value = false },
        onPickedColor = { color ->
            activeColor = color
            println("Active color changed to: ${activeColor.value}")
        }
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            //.horizontalWindowInsetsPadding()
            // .topWindowInsetsPadding()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Bold(state = state)
        ColorizeText(state = state, selectedColor = activeColor)
        ColorizeBg(state = state, selectedColor = activeColor)
        ColorPickerBtn(showColorPicker = showColorPicker)

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextToolButton(
                isSelected = state.isCodeSpan,
                onClick = { state.toggleCodeSpan() },
                icon = Icons.Default.Code, // todo Code_Blocks
                contentDescription = ""
            )
            VerticalDivider(
                modifier = Modifier
                    .heightIn(max = 30.dp)
                    .width(1.dp),
                color = CustomColor.discordDark.copy(alpha = .2f)
            )
            RichTextToolButton(
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Left,
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left)) },
                icon = Icons.AutoMirrored.Filled.FormatAlignLeft,
                contentDescription = ""
            )
        }
        RichTextToolButton(
            isSelected = state.currentParagraphStyle.textAlign == TextAlign.Center,
            onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
            icon = Icons.Default.FormatAlignCenter,
            contentDescription = ""
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextToolButton(
                isSelected = state.currentParagraphStyle.textAlign == TextAlign.Right,
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right)) },
                icon = Icons.AutoMirrored.Filled.FormatAlignRight,
                contentDescription = ""
            )
            VerticalDivider(
                modifier = Modifier
                    .heightIn(max = 30.dp)
                    .width(1.dp),
                color = CustomColor.discordDark.copy(alpha = .2f)
            )
            RichTextToolButton(
                isSelected = state.isUnorderedList,
                onClick = { state.toggleUnorderedList() },
                icon = Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = ""
            )
        }
        RichTextToolButton(
            isSelected = state.isOrderedList,
            onClick = { state.toggleOrderedList() },
            icon = Icons.Default.FormatListNumbered,
            contentDescription = ""
        )

    }
}