package fi.sulku.sulkumail.composables.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.login.buttons.LoginButton
import fi.sulku.sulkumail.composables.login.buttons.RegisterButton
import fi.sulku.sulkumail.composables.login.buttons.SocialArea
import fi.sulku.sulkumail.composables.login.fields.EmailField
import fi.sulku.sulkumail.composables.login.fields.FirstNameField
import fi.sulku.sulkumail.composables.login.fields.LastNameField
import fi.sulku.sulkumail.composables.login.fields.PasswordField

@Composable
fun Login() {
    var isRegistering = remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),

    ) {

        // Login

        // Register
        if (isRegistering.value) {
            FirstNameField(firstName) { firstName = it }
            LastNameField(lastName) { lastName = it }
        }
        EmailField(email) { email = it }
        PasswordField(password, passwordVisible) { password = it }
        Spacer(modifier = Modifier.height(8.dp))
        LoginButton(isRegistering.value)
        SocialArea()
        RegisterButton(isRegistering)
    }
}
