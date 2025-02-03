package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import fi.sulku.sulkumail.Message

@Composable
fun MailItem(detail: Message) {
    val senderName = detail.payload.headers.find { it.name == "From" }?.value ?: "Unknown Sender"
    val subject = detail.payload.headers.find { it.name == "Subject" }?.value ?: "No Subject"
    ListItem(
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon"
            )
        },
        headlineContent = {
            Column {
                Text(
                    text = senderName, // Sender's name at the top
                    style = MaterialTheme.typography.bodyMedium, // Adjust as needed
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subject, // Subject below the sender's name
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        supportingContent = {
            Text(
                text = detail.snippet, // Snippet below
                style = MaterialTheme.typography.bodySmall
            )
        }
    )

}