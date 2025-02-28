package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.di.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val repo: SettingsRepository) : ViewModel() {
//https://dev.to/touchlab/encrypted-key-value-store-in-kotlin-multiplatform-2hnk

    val user: StateFlow<User?> = repo.user
    val mails: StateFlow<SnapshotStateList<UnifiedEmail>> = repo.mails

    suspend fun setUser() {
       // val user: User = google.signIn()
       // repo.setUser(user)
        println("Vm User set")
    }

 /*   suspend fun fetchMails(user: User) {
        val fetchEmails: Flow<UnifiedEmail> = Gmail.fetchMails(user.token)
        fetchEmails.collect { repo.addMail(it) }
    }

     suspend fun trashMail(unifiedMail: UnifiedEmail) {
        repo.trashMail(unifiedMail)
    }*/
}