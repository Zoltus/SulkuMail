package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DrawerTop() {
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "+ New Mail") },
        icon = {},
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = {},
    )
}