package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PromptField(inputText: MutableState<String>) {
    OutlinedTextField(
        value = inputText.value,
        onValueChange = { inputText.value = it },
        label = { Text("Ask ai") },
        modifier = Modifier.Companion
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        singleLine = false,
        maxLines = 8,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF2B2B2B),
            unfocusedContainerColor = Color(0xFF2B2B2B),
            focusedTextColor = Color.Companion.White,
            unfocusedTextColor = Color.Companion.White,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Companion.Gray,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = Color.Companion.Gray,
            unfocusedLabelColor = Color.Companion.Gray
        )
    )
}