package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.data.auth.UserViewModel
import fi.sulku.sulkumail.theme.CustomColor
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MailScreen(
    drawerState: DrawerState
) {
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

            Search(drawerState = drawerState)
            Box(modifier = Modifier.weight(1f)
                .fillMaxWidth()
                .semantics { isTraversalGroup = true }
            ) {
                LazyColumn(
                    state = scrollState,
                    contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.semantics { traversalIndex = 1f },
                ) {
                    items(items = mails, key = { it.id }) { mail ->
                        MailItem(
                            mail = mail,
                            onTrashMail = { mailVm.trashMail(user, mail) }
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
