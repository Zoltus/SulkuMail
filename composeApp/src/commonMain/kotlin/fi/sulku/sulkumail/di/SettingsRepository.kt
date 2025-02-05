package fi.sulku.sulkumail.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import fi.sulku.sulkumail.MessagePage
import fi.sulku.sulkumail.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(private val settings: Settings) {

    init {
        println("TestToken:")
        val get = settings.get<String>("testtoken")
        println("Was: $get")
    }
    //list mails

    private val _token = MutableStateFlow<Token?>(null)
    val token = _token.asStateFlow()

    //todo cache pages
    private val _messagePage = MutableStateFlow<MessagePage?>(null)
    val messagePage = _messagePage.asStateFlow()

    fun setToken(token: Token) {
        _token.value = token
        settings["testtoken"] = token.access_token
    }

    fun setMessagePage(messagePage: MessagePage) {
        _messagePage.value = messagePage
    }
}

data class UserMail(private val s: String) {
    var token : String? = null
    var refreshToken : String? = null
    var pages : List<MessagePage>? = null

    //pageToken,/page?


}