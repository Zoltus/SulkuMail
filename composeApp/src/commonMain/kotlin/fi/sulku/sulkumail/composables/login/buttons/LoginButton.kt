package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginButton(email: String, password: String, captchaToken: String?, isRegistering: Boolean, enabled: Boolean) {
    val authVm = koinViewModel<AuthViewModel>()

    Button(
        enabled = enabled,
        onClick = {
            if (isRegistering) {
                authVm.signUp(email, password, captchaToken)
            } else {
                authVm.signIn(email, password, captchaToken)
            }
        }) {
        val text = if (isRegistering) "Register" else "Login"
        Text(text)
    }
}