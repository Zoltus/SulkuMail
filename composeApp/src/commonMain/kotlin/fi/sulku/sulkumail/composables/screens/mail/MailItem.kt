package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fi.sulku.sulkumail.data.auth.models.room.MailEntity
import fi.sulku.sulkumail.composables.content.DeleteButton

@Composable
fun MailItem(
    mail: MailEntity,
    onTrashMail: () -> Unit
) {
    // Track hover state for the entire list item
    val interactionSource = remember { MutableInteractionSource() }

    SwipeToDelete(
        onDelete = { onTrashMail() }
    ) {
        ListItem(
            modifier = Modifier.hoverable(interactionSource),
            leadingContent = {
                /* Coil Img... */
            },
            headlineContent = {
                Column {
                    Text(
                        text = mail.sender ?: "NoSender",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mail.subject ?: "NoSubject",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            supportingContent = {
                Text(
                    text = mail.snippet ?: "no snippet",
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingContent = {
                DeleteButton(
                    iconDesc = "Delete",
                    interactionSource = interactionSource,
                    onDelete = { onTrashMail() }
                )
            }
        )
    }
}