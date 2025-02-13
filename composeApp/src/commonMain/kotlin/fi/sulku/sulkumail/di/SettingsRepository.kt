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
import fi.sulku.sulkumail.AuthResponse
import fi.sulku.sulkumail.UnifiedEmail
import fi.sulku.sulkumail.viewmodels.Gmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class SettingsRepository(private val settings: Settings) {
    private val _token = MutableStateFlow(settings.decodeValueOrNull(AuthResponse.serializer(), "gtoken"))
    val token = _token.asStateFlow()

    private val _mails = MutableStateFlow<SnapshotStateList<UnifiedEmail>>(SnapshotStateList())
    val mails = _mails.asStateFlow()

    fun setToken(token: AuthResponse) {
        _token.value = token
        settings.encodeValue(AuthResponse.serializer(), "gtoken", token)
    }

    //todo atm no saving of this
    fun addMail(unifiedMail: UnifiedEmail) {
        println("Added mail: ${unifiedMail.id}")
        _mails.value.add(unifiedMail)
    }

    suspend fun trashMail(unifiedMail: UnifiedEmail) {
        Gmail.trashMessage(token.value!!.token, unifiedMail)
    }
}