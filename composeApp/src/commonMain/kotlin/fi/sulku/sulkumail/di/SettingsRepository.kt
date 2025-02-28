@file:OptIn(
    ExperimentalSerializationApi::class, ExperimentalSettingsApi::class, ExperimentalSettingsApi::class,
    ExperimentalSerializationApi::class
)

package fi.sulku.sulkumail.di

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import fi.sulku.sulkumail.composables.screens.manageaccounts.UnifiedEmail
import fi.sulku.sulkumail.composables.screens.manageaccounts.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi

class SettingsRepository(private val settings: Settings) {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _mails = MutableStateFlow<SnapshotStateList<UnifiedEmail>>(SnapshotStateList())
    val mails = _mails.asStateFlow()


    fun setUser(user: User) {
        _user.value = user
        println("USer set $user")
        //settings.encodeValue(AuthResponse.serializer(), "gtoken", authResponse)
    }

/*    //todo atm no saving of this
    fun addMail(unifiedMail: UnifiedEmail) {
        println("Added mail: ${unifiedMail.id}")
        _mails.value.add(unifiedMail)
    }

    suspend fun trashMail(unifiedMail: UnifiedEmail) {
        // todo !!
        Gmail.trashMail(_user.value!!.token, unifiedMail)
    }*/
}