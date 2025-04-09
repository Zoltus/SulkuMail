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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.MailRoute
import fi.sulku.sulkumail.auth.UserViewModel
import fi.sulku.sulkumail.auth.models.Folder
import fi.sulku.sulkumail.auth.models.room.user.User
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.google

@Composable
fun ColumnScope.DrawerMails(
    expandedUsers: SnapshotStateList<User>,
    nav: NavHostController,
    selectedFolder: MutableState<Folder>
) {
    val authVm: UserViewModel = koinViewModel<UserViewModel>()
    val users by authVm.users.collectAsState(initial = emptyList())

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
                    authVm.selectUser(user)
                    nav.navigate(MailRoute)
                    expandedUsers.apply { if (isExpanded) remove(user) else add(user) }
                },
            )
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Folder.entries.forEach {
                        val isSelected = expandedUsers.contains(user) && selectedFolder.value == it
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = it.label) },
                            icon = {
                                Spacer(Modifier.width(20.dp))
                                Icon(imageVector = it.icon, contentDescription = it.contentDesc)
                            },
                            selected = isSelected,
                            badge = { /*Unread amount?*/ },
                            onClick = {
                                authVm.selectUser(user)
                                selectedFolder.value = it
                            },
                        )
                    }
                }
            }
        }
    }
}