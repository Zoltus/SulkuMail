package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.routes.MailEditorRoute

@Composable
fun DrawerTop(nav: NavHostController) {
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "+ New Mail") },
        icon = {},
        selected = false,
        onClick = { nav.navigate(MailEditorRoute) },
    )
}