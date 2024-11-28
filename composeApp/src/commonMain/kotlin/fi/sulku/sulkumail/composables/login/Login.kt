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
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.login.buttons.LoginButton
import fi.sulku.sulkumail.composables.login.buttons.RegisterButton
import fi.sulku.sulkumail.composables.login.buttons.SocialArea
import fi.sulku.sulkumail.composables.login.fields.FirstNameField
import fi.sulku.sulkumail.composables.login.fields.LastNameField
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.ui.ProviderButtonContent
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.EmailField
import io.github.jan.supabase.compose.auth.ui.password.PasswordField
import io.github.jan.supabase.compose.auth.ui.password.PasswordRule
import io.github.jan.supabase.compose.auth.ui.password.rememberPasswordRuleList
import kotlinx.coroutines.launch


@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun Login() {
    val scope = rememberCoroutineScope()
    var isRegistering = remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        // Only on Register
        if (isRegistering.value) {
            FirstNameField(firstName) { firstName = it }
            LastNameField(lastName) { lastName = it }
        }
        //https://supabase.com/docs/guides/auth/auth-identity-linking?queryGroups=language&language=kotlin#manual-linking-beta
        // Login
        EmailField(
            modifier = Modifier.padding(0.dp),
            value = email,
            onValueChange = { email = it },
            label = { Text("E-Mail") },
            mandatory = email.isNotBlank()
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
                scope.launch {
                    //https://supabase.com/docs/reference/kotlin/auth-getuser
                   // supabase.auth.signInWith(Discord)
                    // val session = supabase.auth.currentSessionOrNull()
                    //val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)

                }
            }, //Login with Google,
            content = { ProviderButtonContent(Google) }
        )
        OutlinedButton(
            onClick = {
                //Redirects to:
                scope.launch {
                 //   supabase.auth.signInWith(Discord)
                }
            }, //Login with Twitch,
            content = { ProviderButtonContent(Discord) }
        )

/*        scope.launch {
            supabase.auth.sessionStatus.collect {
                when (it) {
                    is SessionStatus.Authenticated -> {
                        println("Received new authenticated session.")
                        when (it.source) { //Check the source of the session
                            SessionSource.External -> println("External")
                            is SessionSource.Refresh -> println("Refresh")
                            is SessionSource.SignIn -> println("Sign in")
                            is SessionSource.AnonymousSignIn -> println("Anonymous sign in")
                            is SessionSource.SignUp -> println("Sign up")
                            SessionSource.Storage -> println("Storage")
                            SessionSource.Unknown -> println("Unknown")
                            is SessionSource.UserChanged -> println("User changed")
                            is SessionSource.UserIdentitiesChanged -> println("User identities changed")
                        }
                    }

                    SessionStatus.Initializing -> {
                        println("Initializing")
                    }

                    is SessionStatus.RefreshFailure -> {
                        println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                    }

                    is SessionStatus.NotAuthenticated -> {
                        if (it.isSignOut) {
                            println("User signed out")
                        } else {
                            println("User not signed in")
                        }
                    }
                }
            }
        }*/
    }
}

