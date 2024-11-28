package fi.sulku.sulkumail.composables.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.login.buttons.LoginButton
import fi.sulku.sulkumail.composables.login.buttons.RegisterButton
import fi.sulku.sulkumail.composables.login.buttons.SocialArea
import fi.sulku.sulkumail.composables.login.fields.FirstNameField
import fi.sulku.sulkumail.composables.login.fields.LastNameField
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.ui.ProviderButtonContent
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.EmailField
import io.github.jan.supabase.compose.auth.ui.password.PasswordField
import io.github.jan.supabase.compose.auth.ui.password.PasswordRule
import io.github.jan.supabase.compose.auth.ui.password.rememberPasswordRuleList
import org.koin.compose.viewmodel.koinViewModel


@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun Login() {
    var isRegistering = remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authVm = koinViewModel<AuthViewModel>()
    val authErrorMsg by authVm.authErrorMsg.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        // Auth Error message
        authErrorMsg?.let{ Text(text = it, color = Color.Red) }
        // Only on Register
        if (isRegistering.value) {
            FirstNameField(firstName) { firstName = it }
            LastNameField(lastName) { lastName = it }
        }
        // Login
        EmailField(
            modifier = Modifier.padding(0.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text("E-Mail") },
            mandatory = email.isNotBlank(),
        )
        //todo remove random padding?
        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            rules = rememberPasswordRuleList(
                PasswordRule.minLength(6),
                PasswordRule.containsSpecialCharacter(),
                PasswordRule.containsDigit(),
                PasswordRule.containsLowercase(),
                PasswordRule.containsUppercase()
            )
        )
        LoginButton(isRegistering.value)
        RegisterButton(isRegistering)
        SocialArea()

        OutlinedButton(
            onClick = {
            }, //Login with Google,
            content = { ProviderButtonContent(Google) }
        )
        OutlinedButton(
            onClick = {

            }, //Login with Twitch,
            content = { ProviderButtonContent(Discord) }
        )
    }
}

