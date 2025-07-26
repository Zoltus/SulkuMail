package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fi.sulku.sulkumail.composables.content.DeleteButton
import fi.sulku.sulkumail.data.auth.models.room.MailEntity
import fi.sulku.sulkumail.theme.CustomColor

@Composable
fun MailItem(
    mail: MailEntity,
    onClick: () -> Unit,
    onTrashMail: () -> Unit
) {
    // Track hover state for the entire list item
    val interactionSource = remember { MutableInteractionSource() }

    SwipeToDelete(
        onDelete = { onTrashMail() }
    ) {
        MailListItem(
            modifier = Modifier.hoverable(interactionSource)
                .background(CustomColor.discordDark),
            onClick = { onClick() },
            leadingContent = {
                /* Coil Img... */
            },
            headlineContent = {
                Column {
                    Text(
                        text = mail.from,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mail.subject,
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
            },
        )
    }
}
