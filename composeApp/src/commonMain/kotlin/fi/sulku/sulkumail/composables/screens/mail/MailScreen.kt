package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.auth.UserViewModel
import fi.sulku.sulkumail.auth.models.UnifiedEmail
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailScreen(
    drawerState: DrawerState,
    authVm: UserViewModel = koinViewModel()
) {
    val user by authVm.selectedUser.collectAsState()

    user?.let { user ->
        Column {
            val scope = rememberCoroutineScope()

            val mails: SnapshotStateMap<String, UnifiedEmail>? = authVm.getMails(user)

            val scrollState = rememberLazyListState()

            if (mails == null || mails.isEmpty()) {
                Text("No emails")
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
                    items(items = mails.values.toList()) { unifiedMail ->
                        MailItem(
                            message = unifiedMail,
                            onDelete = {
                                scope.launch {
                                    authVm.trashMail(unifiedMail)
                                    println("Trashed")
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
