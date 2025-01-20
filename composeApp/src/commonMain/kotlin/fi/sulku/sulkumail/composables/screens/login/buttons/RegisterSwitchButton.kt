package fi.sulku.sulkumail.composables.screens.login.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun RegisterSwitchButton(isRegistering: MutableState<Boolean>) {
    TextButton(onClick = { isRegistering.value = !isRegistering.value }) {
        Text("Don't have an account? Register")
    }
}