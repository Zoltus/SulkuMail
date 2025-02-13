package fi.sulku.sulkumail.composables.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import fi.sulku.sulkumail.viewmodels.Gmail
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Settings() {
    val authVm = koinViewModel<AuthViewModel>()
    val authResponse by authVm.token.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Settings"
        )
        Spacer(Modifier.height(50.dp))
        Button(onClick = {
            authVm.viewModelScope.launch {
                // todo temp, use vmscope inside gmail, remove vm param? use callback?
                Gmail.authFlow(authVm)
            }

        }) {
            Text("add gmail")
        }

        if (authResponse != null) {
            Button(onClick = {
                authVm.viewModelScope.launch {
                    authVm.fetchMails(authResponse!!.token)
                }
            }) {
                Text("fetchMails")
            }
        }

        Button(onClick = {
            println("mails ${authVm.mails.value}")
        }) {
            Text("viewEmails")
        }
    }
}

