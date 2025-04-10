package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.routes.ManageAccountsRoute
import fi.sulku.sulkumail.routes.SettingsRoute

@Composable
fun DrawerBottom(nav: NavHostController) {
    HorizontalDivider()
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Manage Accounts") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = { nav.navigate(ManageAccountsRoute)},
    )
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Settings") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = { nav.navigate(SettingsRoute) },
    )
}
