package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.data.auth.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MailScreen(
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val userVm = koinInject<UserViewModel>()
    val mailVm = koinViewModel<MailViewModel>()
    val user by userVm.selectedUser.collectAsState()

    user?.let { user ->
        Column {
            val scrollState = rememberLazyListState()
            val mails by mailVm.getMails(user).collectAsState(initial = emptyList())

            if (mails.isEmpty()) {
                Text("No Mails")
                return
            }

            Box(Modifier.semantics { isTraversalGroup = true }) {
                Search(drawerState = drawerState)
                //Mail Content
                LazyColumn(
                    state = scrollState,
                    contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.semantics { traversalIndex = 1f },
                ) {
                    items(items = mails, key = { it.id }) { mail ->
                        MailItem(
                            mail = mail,
                            onTrashMail = {
                                scope.launch {
                                    mailVm.trashMail(user, mail)
                                }
                            }
                        )
                    }
                }
                ScrollBar(scrollState)
            }
        }
    } ?: run {
        Text("No account selected")
    }
}

@Composable
expect fun BoxScope.ScrollBar(scrollState: LazyListState)
