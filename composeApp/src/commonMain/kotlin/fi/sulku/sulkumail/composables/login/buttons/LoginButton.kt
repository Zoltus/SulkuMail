package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fi.sulku.sulkumail.di.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginButton(isRegistering: Boolean) {
    val authVm = koinViewModel<AuthViewModel>()
    val testMail = "zoltus@outlook.com"
    val testPass = "testpas1234"

    Button(onClick = {
        /* Handle login */
        if (isRegistering) {
             authVm.signUp(testMail, testPass)
        } else {
             authVm.signIn(testMail, testPass)
        }
    }) {
        val text = if (isRegistering) "Register" else "Login"
        Text(text)
    }
}