package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//todo supabase to object?
class AuthViewModel(val supabase: SupabaseClient) : ViewModel() {

    private val _user = MutableStateFlow<UserInfo?>(null)
    val user = _user.asStateFlow()

    private val _authErrorMsg = MutableStateFlow<String?>(null)
    val authErrorMsg = _authErrorMsg.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = _user.map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, supabase.auth.currentUserOrNull() != null)

    init {
        viewModelScope.launch {
            supabase.auth.awaitInitialization() // Wait for supabase initialization
            _user.value = supabase.auth.currentUserOrNull() // Set userstate
            // Listen for auth change events
            supabase.auth.sessionStatus.collect { sessionStatus ->
                if (sessionStatus is SessionStatus.Authenticated) {
                    // Set user based on login or signup
                    if (sessionStatus.source is SessionSource.SignIn || sessionStatus.source is SessionSource.SignUp) {
                        _user.value = supabase.auth.currentUserOrNull()
                    }
                } else if (sessionStatus is SessionStatus.NotAuthenticated) {
                    _user.value = null // User is not authenticated, so set null
                }
            }
        }
    }


    fun signOut() {
        catchingAuthAction {
            try {
                supabase.auth.linkIdentity(
                    provider = Google,
                    //For web?, for mobile schema?
                    //
                    redirectUrl = "sulkumail://login"
                ) {
                 queryParams["prompt"] = "consent"
                 automaticallyOpenUrl = true
                scopes.add("email")
                scopes.add("profile")
                 scopes.add("https://www.googleapis.com/auth/userinfo.email")
                 scopes.add("https://www.googleapis.com/auth/userinfo.email")
                  scopes.add("https://www.googleapis.com/auth/gmail.readonly")
                }
                // Handle successful linking
                println("Success?")
            } catch (e: Exception) {
                println("Error " + e.message)
                // Handle error (check e.message)
            }
/*
            val linkId: String? = supabase.auth.linkIdentity(
                //config = ExternalAuthConfigDefaults,
                provider = Google,
                //todo local?
                redirectUrl = "sulkumail://login",
                //
            ) {
                //  queryParams["prompt"] = "consent"
                //  automaticallyOpenUrl = true
                // scopes.add("email")
                // scopes.add("profile")
                //  scopes.add("https://www.googleapis.com/auth/userinfo.email")
                //  scopes.add("https://www.googleapis.com/auth/userinfo.email")
                //   scopes.add("https://www.googleapis.com/auth/gmail.readonly")
            }*/
           // println("@@@@Link $linkId")
            //  linkId?.let { openBrowser(it) }
        }
    }

    fun signUp(email: String, password: String, onSignUp: () -> Unit) {
        //todo loading circle
        catchingAuthAction {
            println("Registering")
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            onSignUp()
            println("Registered")
        }
    }

    fun signIn(email: String, password: String) {
        catchingAuthAction {
            println("Signing in")
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            println("Signed in")
        }
    }

    private fun catchingAuthAction(authAction: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                //todo reset error when switching to register/login screen?
                //Clear old error for new actions
                _authErrorMsg.value = null
                // Execute code
                authAction()
            } catch (authException: AuthRestException) {
                println(authException.message)
                // Set the error message
                _authErrorMsg.value = authException.message
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }
}

/*

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.supabase.supabase.auth.SupabaseAuth
import io.supabase.supabase.auth.providers.Google
import java.awt.Desktop
import java.net.URI

@Composable
fun LinkGmailAccount(supabaseAuth: SupabaseAuth) {
    val context = LocalContext.current

    // Ensure the user is already authenticated
    if (supabaseAuth.currentSession != null) {
        val linkId: String? = supabaseAuth.linkIdentity(
            provider = Google,
            redirectUrl = "sulkumail://linkgmail",
        ) {
            queryParams["prompt"] = "consent"
            automaticallyOpenUrl = true
            scopes.add("https://mail.google.com/")
        }

        // Open the URL in the appropriate platform-specific way
        val authUrl = "https://your-auth-url.com" // Replace with the actual URL

        when {
            Platform.isAndroid -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
                context.startActivity(intent)
            }
            Platform.isIOS -> {
                val nsUrl = NSURL.URLWithString(authUrl)
                UIApplication.sharedApplication.openURL(nsUrl)
            }
            Platform.isDesktop -> {
                Desktop.getDesktop().browse(URI(authUrl))
            }
        }
    } else {
        // Handle the case where the user is not authenticated
        println("User is not authenticated. Please log in first.")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        // Initialize SupabaseAuth
        val supabaseAuth = SupabaseAuth("your-supabase-url", "your-supabase-key")

        // Call the composable function
        LinkGmailAccount(supabaseAuth)
    }
}

Replace "https://your-auth-url.com" with the actual authentication URL you get from Supabase.
Ensure you have the necessary permissions and dependencies for each platform to open URLs.
This setup will allow you to handle authentication prompts on Android, iOS, and Desktop in a Compose Multiplatform app.
 */