package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.google

//todo to viewmodel&Dataclass
data class Mail(
    val title: String,
    val email: String,
)

val mails = listOf(
    Mail("Mail1", "a@outlook.com"),
    Mail("Mail2", "b@gmail.com")
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SideDrawer(
    nav: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

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
                        selected = selectedItemIndex == 0,
                        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                        onClick = {
                            //nav.navigate(mail.email)
                            selectedItemIndex = 0
                        },
                    )
                    mails.forEachIndexed { index, mail ->
                        val isExpanded = expandedMails.contains(mail)
                        /*
                        Mail type and commontypes for dif folder ect and create subitems from those
                        Indox, Spam, Sent, Drafts, Deleted, Archive
                         */
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            //modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            label = {
                                Column {
                                    Text(text = mail.title)
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
                            selected = index + 1 == selectedItemIndex,
                            badge = { /*Unread amount?*/ },
                            onClick = {
                                nav.navigate(mail.email)
                                // Handle mail expanding
                                expandedMails.apply { if (isExpanded) remove(mail) else add(mail) }
                            },
                        )
                        AnimatedVisibility(visible = isExpanded) {
                            Column {
                                NavigationDrawerItem(
                                    shape = MaterialTheme.shapes.small,
                                    label = { Text(text = mail.title) },
                                    icon = {
                                        Spacer(Modifier.width(20.dp))
                                        Icon(imageVector = Icons.Rounded.Inbox, contentDescription = "Inbox")
                                    },
                                    selected = index + 1 == selectedItemIndex,
                                    badge = { /*Unread amount?*/ },
                                    onClick = {

                                    },
                                )
                                NavigationDrawerItem(
                                    shape = MaterialTheme.shapes.small,
                                    label = { Text(text = mail.title) },
                                    icon = {
                                        // Add Spacer before icon so subMenu items are more aligned to right
                                        Spacer(Modifier.width(20.dp))
                                        Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = "Sent")
                                    },
                                    selected = index + 1 == selectedItemIndex,
                                    badge = { /*Unread amount?*/ },
                                    onClick = {

                                    },
                                )
                            }
                        }
                    }

                    //Moves items to bottom
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        HorizontalDivider()
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = "Manage Accounts") },
                            // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                            selected = selectedItemIndex == 0,
                            badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                            onClick = {
                                selectedItemIndex = 0
                            },
                        )

                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = "Settings") },
                            // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                            selected = selectedItemIndex == 0,
                            badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                            onClick = {
                                selectedItemIndex = 0
                            },
                        )
                    }
                }
            }
        }
    )
}