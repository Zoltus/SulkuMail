package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.sulkumail.data.auth.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun UsersList() {
    val scope = rememberCoroutineScope()
    val userVm = koinInject<UserViewModel>()
    val users by userVm.users.collectAsState(initial = emptyList())

    Spacer(Modifier.height(16.dp))
    Text(
        fontSize = 20.sp,
        text = "Your accounts"
    )
    Spacer(Modifier.height(8.dp))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                onDelete = {
                    scope.launch {
                        userVm.removeUser(user)
                    }
                }
            )
        }
    }
}