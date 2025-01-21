package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import fi.sulku.sulkumail.LoginRoute
import fi.sulku.sulkumail.getPlatform
import fi.sulku.sulkumail.mail.Folders
import fi.sulku.sulkumail.mail.Mail
import fi.sulku.sulkumail.mail.MailProviderType
import org.jetbrains.compose.ui.tooling.preview.Preview

//todo to viewmodel&Dataclass
val mails = listOf(
    Mail("Mail1", "a@outlook.com", MailProviderType.OUTLOOK),
    Mail("Mail2", "b@gmail.com", MailProviderType.GMAIL)
)

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
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentDest = navBackStackEntry?.destination
    //All mails which are expanded:
    val expandedMails = remember { mutableStateListOf<Mail>() }

    // Show sidebar only if current route has LoginRoute
    var drawerConent = if (currentDest?.hasRoute(LoginRoute::class) == true) (@Composable {})
    else (@Composable {
        DrawerContent(drawerState, expandedMails, nav, selectedMail, selectedFolder)
    })

    if (getPlatform().isMobile) {
        ModalNavigationDrawer(content = content, drawerContent = drawerConent)
    } else {
        PermanentNavigationDrawer(content = content, drawerContent = drawerConent)
    }
}

@Composable
private fun DrawerContent(
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
                DrawerBottom(nav)
            }
        }
    }
}


