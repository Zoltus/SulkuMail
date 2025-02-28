package fi.sulku.sulkumail.auth

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val repo: UserRepository) : ViewModel() {
//https://dev.to/touchlab/encrypted-key-value-store-in-kotlin-multiplatform-2hnk

    val user: StateFlow<User?> = repo.user
    val mails: StateFlow<SnapshotStateList<UnifiedEmail>> = repo.mails

    fun setUser(user: User) {
        repo.setUser(user)
    }

    suspend fun fetchMails(user: User, query : String) {
        repo.fetchMails(user, query)
    }

    /*
        suspend fun trashMail(unifiedMail: UnifiedEmail) {
           repo.trashMail(unifiedMail)
       }
       */
}