package fi.sulku.sulkumail.composables.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.login.buttons.LoginButton
import fi.sulku.sulkumail.composables.login.buttons.RegisterSwitchButton
import fi.sulku.sulkumail.composables.login.buttons.SocialArea
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.compose.auth.ui.LocalAuthState
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.EmailField
import io.github.jan.supabase.compose.auth.ui.password.PasswordField
import io.github.jan.supabase.compose.auth.ui.password.PasswordRule
import io.github.jan.supabase.compose.auth.ui.password.rememberPasswordRuleList
import org.koin.compose.viewmodel.koinViewModel

@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class, SupabaseInternal::class)
@Composable
fun Login() {
    val formState = LocalAuthState.current
    val authVm = koinViewModel<AuthViewModel>()

    var isRegistering = remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    val authErrorMsg by authVm.authErrorMsg.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        // Auth Error message
        authErrorMsg?.let { Text(text = it, color = Color.Red) }
        // Login
        EmailField(
            value = email,
            label = { Text("Email") },
            onValueChange = { email = it }
        )
        PasswordField(
            value = password,
            label = { Text("Password") },
            onValueChange = { password = it },
            rules = rememberPasswordRuleList(
                PasswordRule.minLength(6),
                PasswordRule.containsSpecialCharacter(),
                PasswordRule.containsDigit(),
                PasswordRule.containsLowercase(),
                PasswordRule.containsUppercase()
            )
        )
        if (isRegistering.value) {
            PasswordField(
                value = passwordConfirm,
                formKey = "PASSWORD_CONFIRM",
                label = { Text("Confirm Password") },
                onValueChange = { passwordConfirm = it },
                rules = rememberPasswordRuleList(
                    PasswordRule("Passwords need to match") { it == password }
                )
            )
        }
        LoginButton(
            email = email,
            password = password,
            isRegistering = isRegistering.value,
            enabled = formState.validForm
        )
        RegisterSwitchButton(isRegistering)
        SocialArea()
    }
}







