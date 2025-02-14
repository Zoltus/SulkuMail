package fi.sulku.sulkumail.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.AuthResponse
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.di.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: SettingsRepository) : ViewModel() {
//https://dev.to/touchlab/encrypted-key-value-store-in-kotlin-multiplatform-2hnk

    val name: StateFlow<String?> = repo.name
    val token: StateFlow<Token?> = repo.token
    val mails: StateFlow<SnapshotStateList<UnifiedEmail>> = repo.mails

    init {
        if (token.value != null) {
            println("Token is not null")

        } else {
            println("Token is null")
        }

        if (token.value != null) {
            viewModelScope.launch {

            }
        }
    }

    suspend fun fetchMails(token: Token) {
        val fetchEmails: Flow<UnifiedEmail> = Gmail.fetchMails(token)
        fetchEmails.collect { repo.addMail(it) }
    }

     suspend fun trashMail(unifiedMail: UnifiedEmail) {
        repo.trashMail(unifiedMail)
    }

    fun setToken(token: AuthResponse) {
        repo.setCredentials(token)
    }
}