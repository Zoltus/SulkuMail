package fi.sulku.sulkumail.composables.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    iconDesc: String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onDelete: () -> Unit
) {
    val isHovered = interactionSource.collectIsHoveredAsState()

    if (isHovered.value) {
        val trashInteraction = remember { MutableInteractionSource() }
        val trashHovered = trashInteraction.collectIsHoveredAsState()

        Box(
            modifier = modifier
                .fillMaxHeight()
                .hoverable(trashInteraction)
                .clickable { onDelete() }
                .background(
                    if (trashHovered.value) Color.Red.copy(alpha = 0.2f)
                    else Color.Transparent
                )
                .padding(8.dp)
                .pointerHoverIcon(PointerIcon.Hand),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = iconDesc,
                tint = if (trashHovered.value) Color.Red
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}