@file:OptIn(
    ExperimentalSerializationApi::class, ExperimentalSettingsApi::class, ExperimentalSettingsApi::class,
    ExperimentalSerializationApi::class
)

package fi.sulku.sulkumail.di

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import fi.sulku.sulkumail.Message
import fi.sulku.sulkumail.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class SettingsRepository(private val settings: Settings) {
    private val _token = MutableStateFlow(settings.decodeValueOrNull(Token.serializer(), "gtoken"))
    val token = _token.asStateFlow()

    fun setToken(token: Token) {
        _token.value = token
        settings.encodeValue(Token.serializer(), "gtoken", token)
    }

    private val _messagePage = MutableStateFlow<MessagePage2?>(null)
    val messagePage = _messagePage.asStateFlow()

    //private val _messagePage2 = MutableStateFlow<SnapshotStateList<String>>(SnapshotStateList())
   // val messagePage2 = _messagePage2.asStateFlow()

    fun setMessagePage(messagePage: MessagePage2) {
        _messagePage.value = messagePage

        //todo atm no saving of this
    }

}



data class MessagePage2(
    val pageToken: String? = null,
    val messages: SnapshotStateList<Message> = SnapshotStateList(),
)
