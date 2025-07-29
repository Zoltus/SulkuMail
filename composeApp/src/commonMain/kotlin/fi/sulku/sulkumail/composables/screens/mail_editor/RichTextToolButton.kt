package fi.sulku.sulkumail.composables.screens.mail_editor

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import fi.sulku.sulkumail.theme.CustomColor

@Composable
fun RichTextToolButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
    contentDescription: String? = null
) {
    val buttonContentColor: Color by animateColorAsState(
        targetValue = if (isSelected) CustomColor.discordDark else CustomColor.discordLight,
        label = "buttonContentColor"
    )

    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (isSelected) CustomColor.discordLightest else Color.Transparent,
        label = "buttonBackgroundColor"
    )

    IconButton(
        modifier = Modifier
            // Workaround to prevent the rich editor
            // from losing focus when clicking on the button
            // (Happens only on Desktop)
            .focusProperties { canFocus = false },
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (isSelected) {
                buttonContentColor
            } else {
                buttonBackgroundColor
            },
        ),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = tint ?: buttonContentColor,
            modifier = Modifier
                .background(
                    color = if (isSelected) {
                        buttonContentColor
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        )
    }
}
