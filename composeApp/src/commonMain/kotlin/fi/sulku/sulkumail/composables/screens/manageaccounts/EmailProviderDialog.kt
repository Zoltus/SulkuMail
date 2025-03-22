package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.auth.EmailProvider

@Composable
fun EmailProviderDialog(
    onDismiss: () -> Unit,
    onProviderSelected: (EmailProvider) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Email Provider") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProviderCard(
                    icon = Icons.Default.Email,
                    text = "Gmail",
                    secondatyText = "Google Account",
                    onClick = { onProviderSelected(EmailProvider.GMAIL) }
                )
                ProviderCard(
                    icon = Icons.Default.Mail,
                    text = "Outlook",
                    secondatyText = "Outkook, Hotmail, Live, MSN",
                    onClick = { onProviderSelected(EmailProvider.OUTLOOK) }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}