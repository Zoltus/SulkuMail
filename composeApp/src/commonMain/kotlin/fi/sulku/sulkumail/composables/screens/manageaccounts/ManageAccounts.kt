package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManageAccounts() {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Manage accounts"
        )
        Spacer(Modifier.height(50.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            onClick = { showDialog = true }
        ) {
            Column(
                // modifier = Modifier
                //.clickable(onClick = onClick)
                //     .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Account",
                    modifier = Modifier.size(50.dp)
                        .padding(bottom = 8.dp),
                )
                Text("Add Account")
            }
        }
    }

    if (showDialog) {
        EmailProviderDialog(
            onDismiss = { showDialog = false },
            onProviderSelected = { provider ->
                showDialog = false
            }
        )
    }
}

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
                ProviderCard(
                    icon = Icons.Default.MailOutline,
                    text = "IMAP/SMTP",
                    secondatyText = "Custom IMAP/SMTP Server",
                    onClick = { onProviderSelected(EmailProvider.CUSTOM) }
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

@Composable
private fun ProviderCard(
    text: String,
    secondatyText: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, "Desc")
            //Texts
            Column(Modifier.padding(start = 8.dp)) {
                Text(text)
                Text(secondatyText, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

enum class EmailProvider {
    GMAIL, OUTLOOK, CUSTOM
}