package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.ResponsesArea(messages: List<String>) {
    LazyColumn(
        modifier = Modifier.Companion
            .align(Alignment.Companion.End)
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Companion.Bottom),
        reverseLayout = false
    ) {
        items(messages) { message ->
            ResponseField(message)
        }
    }
}