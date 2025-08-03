package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun ResponseField(
    message: String,
) {
    // Deprecated, new api hasn't fully implemented cross platform yet.
    val clipboard = LocalClipboardManager.current
    Box {
        TextField(
            value = message,
            readOnly = true,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
        )

        IconButton(
            onClick = { clipboard.setText(AnnotatedString(message)) },
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

