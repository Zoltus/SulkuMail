package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.UserViewModel
import fi.sulku.sulkumail.routes.MailRoute
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.google

@Composable
fun ColumnScope.DrawerMails(nav: NavHostController) {
    val userVm = koinInject<UserViewModel>()
    val drwVm = koinViewModel<DrawerViewModel>()

    val users by userVm.users.collectAsState(initial = emptyList())
    val selectedFolder by drwVm.selectedUserFolder.collectAsState()
    val expandedUsers by drwVm.expandedUsers.collectAsState()

    LazyColumn(modifier = Modifier.weight(1f)) {
        items(users) { user ->
            val userInfo = user.userInfo
            val isExpanded = expandedUsers.contains(user)
            NavigationDrawerItem(
                selected = false,
                shape = MaterialTheme.shapes.small,
                label = {
                    Column {
                        Text(text = userInfo.name)
                        Text(
                            text = userInfo.email,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.google), contentDescription = "MailIcon",
                        modifier = Modifier.size(18.dp),
                    )
                },
                badge = {
                    val arrow = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    Icon(arrow, contentDescription = "Expand folder arrow")
                },
                onClick = {
                    userVm.selectUser(user)
                    nav.navigate(MailRoute)
                    drwVm.toggleUserExpansion(user)
                },
            )
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Folder.entries.forEach { folder ->
                        val isSelected = user to folder == selectedFolder
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = folder.label) },
                            icon = {
                                Spacer(Modifier.width(20.dp))
                                Icon(imageVector = folder.icon, contentDescription = folder.contentDesc)
                            },
                            selected = isSelected,
                            badge = { /*Unread amount?*/ },
                            onClick = {
                                userVm.selectUser(user)
                                drwVm.selectFolder(user, folder)
                            },
                        )
                    }
                }
            }
        }
    }
}