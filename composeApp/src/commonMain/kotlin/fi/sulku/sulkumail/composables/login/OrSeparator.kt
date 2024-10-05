package fi.sulku.sulkumail.composables.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrSeparator() {
    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
        HorizontalDivider(modifier = Modifier.Companion.weight(1f))
        Spacer(modifier = Modifier.Companion.width(8.dp))
        Text("Or")
        Spacer(modifier = Modifier.Companion.width(8.dp))
        HorizontalDivider(modifier = Modifier.Companion.weight(1f))
    }
}