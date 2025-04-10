package fi.sulku.sulkumail.data.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val userRepo: UserRepository) : ViewModel() {

    private val scopes: List<String> = listOf(
        "email",
        "profile",
        "https://www.googleapis.com/auth/gmail.modify"
    )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun startGoogleAuth() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    val token: Token = startGoogleAuthFlow(scopes)
                    userRepo.createUser(token)
                }
                _uiState.value = AuthUiState.Success
            } catch (e: AuthException) {
                println("Auth exception: ${e.message}")
                _uiState.value = AuthUiState.Error(e.message)
            } catch (e: Exception) {
                println("Error during auth/user creation: ${e.message}")
                _uiState.value = AuthUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    data object Idle : AuthUiState() // Initial state
    data object Loading : AuthUiState() // Auth in progress
    data object Success : AuthUiState() // Auth succeeded, user created
    data class Error(val message: String) : AuthUiState() // Auth failed
}
