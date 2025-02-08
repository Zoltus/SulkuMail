package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.di.MessagePage2
import fi.sulku.sulkumail.di.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: SettingsRepository) : ViewModel() {
//https://dev.to/touchlab/encrypted-key-value-store-in-kotlin-multiplatform-2hnk

    val token: StateFlow<Token?> = repo.token
    val messagePage: StateFlow<MessagePage2?> = repo.messagePage

    init {
        if (token.value != null) {
            println("Token is not null")

        } else {
            println("Token is null")
        }

        if (token.value != null && messagePage.value == null) {
            viewModelScope.launch {
                println("Starting fetch")
                val messagePage = Gmail.fetchPage(token.value!!, null) //todo !! and multifetches
                repo.setMessagePage(messagePage)
                println("Fetch Done and saved")
            }
        }
    }

    fun setToken(token: Token) {
        repo.setToken(token)
    }

    fun setMessagePage(messagePage: MessagePage2) {
        repo.setMessagePage(messagePage)
    }


}