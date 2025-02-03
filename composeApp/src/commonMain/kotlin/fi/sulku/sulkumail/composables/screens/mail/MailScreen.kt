package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.Message
import fi.sulku.sulkumail.MessagesResp
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailScreen(drawerState: DrawerState, email: String) {
    Column {
        val authVm = koinViewModel<AuthViewModel>()
        val messageResp by authVm.emailDetails.collectAsState()
        val msResp: MessagesResp? = messageResp
        val t: List<Message>? = msResp?.messages

        if (msResp == null || t == null || t.isEmpty()) {
            Text("No emails")
            return
        }

        Box(Modifier.semantics { isTraversalGroup = true }) {
            Search(messageResp = msResp, drawerState = drawerState)
            //Content
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics { traversalIndex = 1f },
            ) {
                items(msResp.messages) { MailItem(it) }
            }
        }
    }
}