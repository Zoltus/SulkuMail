package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.mail.Folders
import fi.sulku.sulkumail.mail.Mail
import fi.sulku.sulkumail.mail.MailProviderType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.google

//todo to viewmodel&Dataclass
val mails = listOf(
    Mail("Mail1", "a@outlook.com", MailProviderType.OUTLOOK),
    Mail("Mail2", "b@gmail.com", MailProviderType.GMAIL)
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SideDrawer(
    nav: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    //todo to vm
    //todo selectedMail and selected folder
    var selectedMail by remember { mutableStateOf(mails[0]) }
    var selectedFolder by remember { mutableStateOf(Folders.Inbox) }

    //All mails which are expanded:
    val expandedMails = remember { mutableStateListOf<Mail>() }

    PermanentNavigationDrawer(
        content = content,
        drawerContent = {
            AnimatedVisibility(
                visible = drawerState.isOpen,
                enter = expandHorizontally(animationSpec = tween(durationMillis = 200)),
                exit = shrinkHorizontally(animationSpec = tween(durationMillis = 200))
            ) {
                PermanentDrawerSheet(
                    modifier = Modifier.width(280.dp),
                ) {
                    NavigationDrawerItem(
                        shape = MaterialTheme.shapes.small,
                        label = { Text(text = "+ New Mail") },
                        icon = {},
                        selected = false,
                        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                        onClick = {},
                    )
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(mails) { mail ->
                            val isExpanded = expandedMails.contains(mail)
                            NavigationDrawerItem(
                                shape = MaterialTheme.shapes.small,
                                label = {
                                    Column {
                                        Text(text = mail.label)
                                        Text(
                                            text = mail.email,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.google), contentDescription = "MailIcon",
                                        modifier = Modifier.Companion.size(18.dp),
                                    )
                                },
                                selected = false,
                                badge = { /*Unread amount?*/ },
                                onClick = {
                                    nav.navigate(mail.email)
                                    // Handle mail expanding
                                    expandedMails.apply { if (isExpanded) remove(mail) else add(mail) }
                                },
                            )
                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    Folders.entries.forEach {
                                        val isSelected = selectedMail == mail && selectedFolder == it
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
                                                selectedMail = mail
                                                selectedFolder = it
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }

                    //Moves items to bottom
                    Column(
                        verticalArrangement = Arrangement.Bottom
                    ) {
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
                }
            }
        }
    )
}