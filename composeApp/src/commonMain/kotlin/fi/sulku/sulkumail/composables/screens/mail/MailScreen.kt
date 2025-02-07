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
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.Message
import fi.sulku.sulkumail.MessagePage
import fi.sulku.sulkumail.composables.screens.settings.trashMessage
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailScreen(drawerState: DrawerState, email: String) {
    Column {
        val scope = rememberCoroutineScope()



        val authVm = koinViewModel<AuthViewModel>()

        val token by authVm.token.collectAsState()

        val messageResp by authVm.messagePage.collectAsState()
        val msResp: MessagePage? = messageResp
        val t: List<Message>? = msResp?.messages

        val scrollState = rememberLazyListState()

        if (msResp == null || t.isNullOrEmpty()) {
            Text("No emails")
            return
        }

        Box(Modifier.semantics { isTraversalGroup = true }) {
            Search(messageResp = msResp, drawerState = drawerState)
            //Mail Content
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics { traversalIndex = 1f },
            ) {
                items(msResp.messages) { MailItem(it, onDelete = {
                    scope.launch {
                        token?.let { it1 -> trashMessage(it1, it) }
                    }
                }) }
            }
            ScrollBar(scrollState)
        }
    }
}

@Composable
expect fun BoxScope.ScrollBar(scrollState: LazyListState)
