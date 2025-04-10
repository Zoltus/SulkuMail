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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.UserViewModel
import fi.sulku.sulkumail.composables.screens.mail.MailViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Settings() {
    val userVm = koinInject<UserViewModel>()
    val mailVm = koinViewModel<MailViewModel>()
    val user by userVm.selectedUser.collectAsState()
    val usera = user
    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Settings"
        )
        Spacer(Modifier.height(50.dp))

        usera?.let { user ->
            Button(onClick = {
                mailVm.fetchMail(user, Folder.Inbox)
            }) {
                Text("Fetch mails")
            }
              Button(onClick = {
                mailVm.fetchMail(user, Folder.Trash)
            }) {
                Text("Fetch Trash")
            }
        }
    }
}


