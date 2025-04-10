package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.sulkumail.data.auth.AuthException
import fi.sulku.sulkumail.data.auth.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageAccounts() {
    val authVm = koinViewModel<AuthViewModel>()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Manage accounts"
        )
        Spacer(Modifier.height(50.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            onClick = { showDialog = true }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Account",
                    modifier = Modifier.size(50.dp)
                        .padding(bottom = 8.dp),
                )
                Text("Add Account")
            }
        }
        UsersList()
    }

    if (showDialog) {
        EmailProviderDialog(
            onDismiss = { showDialog = false },
            onProviderSelected = { provider -> // todo provider
                showDialog = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        authVm.startGoogleAuth()
                    } catch (e: AuthException) {
                        //todo popup dialog
                        println("Auth exception " + e.message)
                    }
                }

            }
        )
    }
}

