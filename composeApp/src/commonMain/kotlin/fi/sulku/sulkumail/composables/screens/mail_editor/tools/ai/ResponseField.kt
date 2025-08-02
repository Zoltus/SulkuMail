package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponseField(
    message: String,
    onCopy: () -> Unit = {}
) {
    Box {
        TextField(
            value = message,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
        )

        IconButton(
            onClick = onCopy,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

