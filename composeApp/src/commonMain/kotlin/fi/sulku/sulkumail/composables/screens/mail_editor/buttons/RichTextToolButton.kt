package fi.sulku.sulkumail.composables.screens.mail_editor.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.theme.CustomColor

@Composable
fun RichTextToolButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
    contentDescription: String? = null
) {
    val buttonBackgroundColor: Color by animateColorAsState(
        targetValue = if (isSelected) CustomColor.discordBlue2 else Color.Transparent,
        label = "buttonBackgroundColor"
    )

    val iconColor: Color by animateColorAsState(
        targetValue = if (isSelected) Color.White else CustomColor.discordLight2,
        label = "iconColor"
    )

    IconButton(
        modifier = Modifier
            .background(
                color = buttonBackgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
            // Workaround to prevent the rich editor
            // from losing focus when clicking on the button
            // (Happens only on Desktop)
            .focusProperties { canFocus = false },
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = iconColor,
            containerColor = Color.Transparent
        ),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = tint ?: iconColor
        )
    }
}
