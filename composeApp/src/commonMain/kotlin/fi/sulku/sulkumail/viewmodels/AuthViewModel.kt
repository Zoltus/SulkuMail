package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//todo supabase to object?
class AuthViewModel(private val supabase: SupabaseClient) : ViewModel() {

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
            println("logging out")
            supabase.auth.signOut()
            println("logged out")
        }
    }

    fun signUp(email: String, password: String) {
        catchingAuthAction {
            println("Registering")
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
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