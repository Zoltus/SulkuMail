package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun RowScope.PromptButton(aiVm: AiViewModel, inputText: MutableState<String>) {
    val isLoading by aiVm.isLoading.collectAsState()
    Button(
        enabled = !isLoading,
        onClick = {
            aiVm.askAi(inputText.value)
            inputText.value = ""
        },
        modifier = Modifier.Companion.weight(1f) // todo ?
    ) {
        val label = if (isLoading) "Working.." else "Generate"
        Text(label)
    }
}