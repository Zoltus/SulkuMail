package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.EmailDetail
import fi.sulku.sulkumail.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel() : ViewModel() {

    private val _tempToken = MutableStateFlow<Token?>(null)
    val tempToken = _tempToken.asStateFlow()

    private val _nextPageToken = MutableStateFlow<String>("")
    val nextPageToken = _nextPageToken.asStateFlow()

    private val _emailDetails = MutableStateFlow<List<EmailDetail>>(emptyList())
    val emailDetails = _emailDetails.asStateFlow()

    init {}

    fun setTempToken(token: Token) {
        _tempToken.value = token
    }

    fun setNextPageToken(token: String) {
        _nextPageToken.value = token
    }

    fun setEmailDetails(emailDetailsList: List<EmailDetail>) {
        _emailDetails.value = emailDetailsList
        println("Detials set to:$emailDetailsList")
    }
}