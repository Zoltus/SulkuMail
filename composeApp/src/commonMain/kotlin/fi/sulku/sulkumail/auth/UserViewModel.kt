package fi.sulku.sulkumail.auth

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.models.UnifiedEmail
import fi.sulku.sulkumail.auth.models.User
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    val users: SnapshotStateList<User> = repo.users
    val selectedUser: StateFlow<User?> = repo.selectedUser

    private suspend fun createUser(token: Token) = repo.createUser(token)

    fun selectUser(user: User) = repo.selectUser(user)

    suspend fun fetchMails(user: User, query: String = "") = repo.fetchMails(user, query)

    suspend fun trashMail(unifiedMail: UnifiedEmail) = repo.trashMail(selectedUser.value, unifiedMail)

    fun getMails(user: User): SnapshotStateMap<String, UnifiedEmail> = repo.getMails(user)

    suspend fun startGoogleAuth(): User { // todo scope?
        val scopes: List<String> = listOf(
            "email",
            "profile",
            "https://www.googleapis.com/auth/gmail.modify"
        )
        val token = startGoogleAuthFlow(scopes)
        val user = createUser(token)
        return user
    }
}