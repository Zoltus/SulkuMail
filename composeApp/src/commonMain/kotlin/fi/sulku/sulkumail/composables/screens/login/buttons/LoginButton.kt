package fi.sulku.sulkumail.composables.screens.login.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginButton(
    email: String,
    password: String,
    isRegistering: MutableState<Boolean>,
    enabled: Boolean
) {
    val authVm = koinViewModel<AuthViewModel>()

    Button(
        enabled = enabled,
        onClick = {
            if (isRegistering.value) {
                authVm.signUp(email, password) {
                    isRegistering.value = false
                }
            } else {
                authVm.signIn(email, password)
            }
        }) {
        val text = if (isRegistering.value) "Register" else "Login"
        Text(text)
    }
}