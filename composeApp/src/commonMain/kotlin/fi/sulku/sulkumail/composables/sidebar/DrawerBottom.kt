package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DrawerBottom() {
    HorizontalDivider()
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Manage Accounts") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = {
        },
    )
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Settings") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = {},
    )
}