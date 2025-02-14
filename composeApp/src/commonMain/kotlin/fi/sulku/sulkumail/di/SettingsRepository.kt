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
import fi.sulku.sulkumail.viewmodels.Gmail
import fi.sulku.sulkumail.viewmodels.UnifiedEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class SettingsRepository(private val settings: Settings) {
    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _token = MutableStateFlow(settings.decodeValueOrNull(AuthResponse.serializer(), "gtoken")?.token)
    val token = _token.asStateFlow()

    private val _mails = MutableStateFlow<SnapshotStateList<UnifiedEmail>>(SnapshotStateList())
    val mails = _mails.asStateFlow()

    fun setCredentials(authResponse: AuthResponse) {
        _name.value = authResponse.emailAdress
        _token.value = authResponse.token
        settings.encodeValue(AuthResponse.serializer(), "gtoken", authResponse)
    }

    //todo atm no saving of this
    fun addMail(unifiedMail: UnifiedEmail) {
        println("Added mail: ${unifiedMail.id}")
        _mails.value.add(unifiedMail)
    }

    suspend fun trashMail(unifiedMail: UnifiedEmail) {
        // todo !!
        Gmail.trashMail(token.value!!, unifiedMail)
    }
}