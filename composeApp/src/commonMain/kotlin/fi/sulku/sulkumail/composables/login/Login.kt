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
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.compose.auth.ui.ProviderButtonContent
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.EmailField
import io.github.jan.supabase.compose.auth.ui.password.PasswordField
import io.github.jan.supabase.compose.auth.ui.password.PasswordRule
import io.github.jan.supabase.compose.auth.ui.password.rememberPasswordRuleList
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.launch


@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun Login() {
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

        val scope = rememberCoroutineScope()
        //todo to env
        val supabase = createSupabaseClient(
            supabaseUrl = "https://obvebhxdnmbnzjbinsgw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9idmViaHhkbm1ibnpqYmluc2d3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyODM5ODc1OSwiZXhwIjoyMDQzOTc0NzU5fQ.tTjla5vP5CjFmSUFjcfrB-ILmbVKlXlaYqssuLCyi5E"
        ) {
            install(Auth) {
                host = "sulkumail"
                scheme = "login"

                // On Android only, you can set OAuth and SSO logins to open in a custom tab, rather than an external browser:
                // defaultExternalAuthAction = ExternalAuthAction.CustomTabs() //defaults to ExternalAuthAction.ExternalBrowser
            }
            /*install(ComposeAuth) { // ios/android native google auth
                googleNativeLogin(serverClientId = "google-client-id")
                appleNativeLogin()
            }*/
            //install(Postgrest)
        }

        OutlinedButton(
            onClick = {
                scope.launch {
                    //https://supabase.com/docs/reference/kotlin/auth-getuser
                    supabase.auth.signInWith(Discord)
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
                    supabase.auth.signInWith(Discord)
                }
            }, //Login with Twitch,
            content = { ProviderButtonContent(Discord) }
        )

        scope.launch {
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
        }
    }
}

/*
val json = Json { ignoreUnknownKeys = true }

val client = HttpClient() {
    install(ContentNegotiation) { json }
}

val dotenv = dotenv()
val clientId: String = dotenv["DISCORD_CLIENT_ID"]
val clientSecret: String = dotenv["DISCORD_CLIENT_SECRET"]
val redirectUri: String = dotenv["DISCORD_REDIRECT_URI"]
*/

