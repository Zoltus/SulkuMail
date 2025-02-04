package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.MessagePage
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.di.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(val repo: SettingsRepository) : ViewModel() {
//https://dev.to/touchlab/encrypted-key-value-store-in-kotlin-multiplatform-2hnk

    val token: StateFlow<Token?> = repo.token
    val messagePage: StateFlow<MessagePage?> = repo.messagePage

    fun setToken(token: Token) {
        repo.setToken(token)
    }

    fun setMessagePage(messagePage: MessagePage) {
        repo.setMessagePage(messagePage)
    }


}