package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginButton(isRegistering: Boolean) {
    Button(onClick = { /* Handle login */ }) {
        val text = if (isRegistering) "Register" else "Login"
        Text(text)
    }
}