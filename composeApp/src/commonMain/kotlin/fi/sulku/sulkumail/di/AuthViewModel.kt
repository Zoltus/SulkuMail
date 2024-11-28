package fi.sulku.sulkumail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class AuthViewModel(private val supabaseClient: SupabaseClient) : ViewModel() {

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                println("Registering")
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                println("Registered")
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }

   fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                println("signing in")
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                println("signed in")
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }
}