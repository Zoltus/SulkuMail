package fi.sulku.sulkumail.viewmodels

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.EmailDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel() : ViewModel() {

    private val _tempToken = MutableStateFlow<String>("")
    val tempToken = _tempToken.asStateFlow()

    private val _nextPageToken = MutableStateFlow<String>("")
    val nextPageToken = _nextPageToken.asStateFlow()

    private val _emailDetails = MutableStateFlow<List<EmailDetail>>(emptyList())
    val emailDetails = _emailDetails.asStateFlow()

    init {

    }
}