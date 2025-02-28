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
import androidx.navigation.NavHostController
import fi.sulku.sulkumail.auth.User
import fi.sulku.sulkumail.auth.UserViewModel
import fi.sulku.sulkumail.getPlatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

//todo to viewmodel&Dataclass
@Preview
@Composable
fun SideDrawer(
    nav: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val authVm: UserViewModel = koinViewModel<UserViewModel>()
    val user by authVm.user.collectAsState()
    //todo to vm
    //todo selectedMail and selected folder

    //val navBackStackEntry by nav.currentBackStackEntryAsState()
    //val currentDest = navBackStackEntry?.destination

    val selectedUser = remember { mutableStateOf(user) }
    val selectedFolder = remember { mutableStateOf(Folders.Inbox) }
    //All mails which are expanded:
    val expandedUserFolders = remember { mutableStateListOf<User>() }

    // Show sidebar only if current route has LoginRoute
    val drawerConent = @Composable {
        DrawerContent(drawerState, expandedUserFolders, nav, selectedUser, selectedFolder)
    }
    if (getPlatform().isMobile) {
        ModalNavigationDrawer(content = content, drawerContent = drawerConent)
    } else {
        PermanentNavigationDrawer(content = content, drawerContent = drawerConent)
    }

    LaunchedEffect(user) {
        selectedUser.value = user
    }
}

@Composable
private fun DrawerContent(
    drawerState: DrawerState,
    expandedUsers: SnapshotStateList<User>,
    nav: NavHostController,
    selectedUser: MutableState<User?>,
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
            DrawerMails(expandedUsers, nav, selectedUser, selectedFolder)

            //Bottom nav
            Column(verticalArrangement = Arrangement.Bottom) {
                DrawerBottom(nav)
            }
        }
    }
}


