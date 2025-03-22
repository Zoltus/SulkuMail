package fi.sulku.sulkumail.auth

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.models.room.user.User
import fi.sulku.sulkumail.auth.models.room.user.MailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    val users: Flow<List<User>> = repo.getUsers()
    val selectedUser: StateFlow<User?> = repo.selectedUser

    private suspend fun createUser(token: Token) = repo.createUser(token)

    fun selectUser(user: User) = repo.selectUser(user)

    suspend fun removeUser(user: User) = repo.removeUser(user)

    fun getMails(user: User): Flow<List<MailEntity>> = repo.getMails(user)

    suspend fun fetchMails(user: User) = repo.fetchMails(user)

    suspend fun trashMail(mail: MailEntity) = repo.trashMail(selectedUser.value, mail)

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