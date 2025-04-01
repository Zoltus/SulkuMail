package fi.sulku.sulkumail.composables.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.sulkumail.auth.UserViewModel
import fi.sulku.sulkumail.auth.models.room.user.User

@Composable
fun Settings(authVm: UserViewModel) {
    val user: State<User?> = authVm.selectedUser.collectAsState()
    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Settings"
        )
        Spacer(Modifier.height(50.dp))

        user.value?.let {
            Button(onClick = {
                authVm.fetchMails(it)
            }) {
                Text("Fetch mails")
            }
        }
    }
}


