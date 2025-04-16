package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MailListItem(
    modifier: Modifier = Modifier.Companion,
    leadingContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    verticalAligment: Alignment.Vertical = Alignment.Companion.CenterVertically,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp),
        verticalAlignment = verticalAligment
    ) {
        if (leadingContent != null) {
            leadingContent()
        }
        Column(
            modifier = Modifier.Companion.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (headlineContent != null) {
                headlineContent()
            }
            if (supportingContent != null) {
                Spacer(modifier = Modifier.Companion.height(4.dp))
                supportingContent()
            }
        }
        if (trailingContent != null) {
            trailingContent()
        }
        Spacer(modifier = Modifier.Companion.height(100.dp))
    }
}