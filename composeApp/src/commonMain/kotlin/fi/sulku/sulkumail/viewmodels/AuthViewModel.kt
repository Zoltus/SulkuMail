package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.MessagesResp
import fi.sulku.sulkumail.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel() : ViewModel() {

    private val _tempToken = MutableStateFlow<Token?>(null)
    val tempToken = _tempToken.asStateFlow()

    private val _emailDetails = MutableStateFlow<MessagesResp?>(null)
    val emailDetails = _emailDetails.asStateFlow()

    init {}

    fun setTempToken(token: Token) {
        _tempToken.value = token
    }


    fun setEmailDetails(emailDetailsList: MessagesResp) {
        _emailDetails.value = emailDetailsList
        println("Detials set to:$emailDetailsList")
    }
}