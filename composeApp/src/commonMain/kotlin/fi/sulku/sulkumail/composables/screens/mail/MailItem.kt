package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.auth.UserViewModel
import fi.sulku.sulkumail.auth.models.room.user.MailEntity
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MailItem(
    unifiedMail: MailEntity,
    authVm: UserViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    // Track hover state for the entire list item
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()

    ListItem(
        modifier = Modifier.hoverable(interactionSource),
        leadingContent = {
            /* Coil Img... */
        },
        headlineContent = {
            Column {
                Text(
                    text = unifiedMail.sender ?: "NoSender",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = unifiedMail.subject ?: "NoSubject",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        supportingContent = {
            Text(
                text = unifiedMail.snippet ?: "no snippet",
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingContent = {
            if (isHovered.value) {
                // Track hover state for the trash icon
                val trashInteraction = remember { MutableInteractionSource() }
                val trashHovered = trashInteraction.collectIsHoveredAsState()

                Box(
                    modifier = Modifier
                        .hoverable(trashInteraction)
                        .clickable {
                            scope.launch {
                                authVm.trashMail(unifiedMail)
                                println("Trashed")
                            }
                        }
                        .background(
                            if (trashHovered.value) Color.Red.copy(alpha = 0.2f)
                            else Color.Transparent
                        )
                        .padding(8.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete email",
                        tint = if (trashHovered.value) Color.Red
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}