package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.getPlatform
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
    val selectedMail = remember { mutableStateOf(mails[0]) }
    val selectedFolder = remember { mutableStateOf(Folders.Inbox) }

    //All mails which are expanded:
    val expandedMails = remember { mutableStateListOf<Mail>() }
    val drawerConent = @Composable {
        DrawerConent(drawerState, expandedMails, nav, selectedMail, selectedFolder)
    }

    if (getPlatform().isMobile) {
        ModalNavigationDrawer(content = content, drawerContent = drawerConent)
    } else {
        PermanentNavigationDrawer(content = content, drawerContent = drawerConent)
    }
}

@Composable
private fun DrawerConent(
    drawerState: DrawerState,
    expandedMails: SnapshotStateList<Mail>,
    nav: NavHostController,
    selectedMail: MutableState<Mail>,
    selectedFolder: MutableState<Folders>
) {
    AnimatedVisibility(
        visible = drawerState.isOpen,
        enter = expandHorizontally(animationSpec = tween(durationMillis = 200)),
        exit = shrinkHorizontally(animationSpec = tween(durationMillis = 200))
    ) {
        PermanentDrawerSheet(
            modifier = Modifier.width(280.dp),
        ) {
            DrawerTop()
            DrawerMails(expandedMails, nav, selectedMail, selectedFolder)
            //Moves items to bottom
            Column(verticalArrangement = Arrangement.Bottom) {
                DrawerBottom()
            }
        }
    }
}


@Composable
fun ColumnScope.DrawerMails(
    expandedMails: SnapshotStateList<Mail>,
    nav: NavHostController,
    selectedMail: MutableState<Mail>,
    selectedFolder: MutableState<Folders>
) {
    LazyColumn(modifier = Modifier.weight(1f)) {
        items(mails) { mail ->
            val isExpanded = expandedMails.contains(mail)
            NavigationDrawerItem(
                selected = false,
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
                        modifier = Modifier.size(18.dp),
                    )
                },
                badge = {
                    val arrow = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    Icon(arrow, contentDescription = "Expand folder arrow")
                },
                onClick = {
                    nav.navigate(mail.email)
                    expandedMails.apply { if (isExpanded) remove(mail) else add(mail) }
                },
            )
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Folders.entries.forEach {
                        val isSelected = selectedMail.value == mail && selectedFolder.value == it
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
                                selectedMail.value = mail
                                selectedFolder.value = it
                            },
                        )
                    }
                }
            }
        }
    }
}