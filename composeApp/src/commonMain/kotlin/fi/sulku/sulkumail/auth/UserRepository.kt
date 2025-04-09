package fi.sulku.sulkumail.auth

import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.models.Folder
import fi.sulku.sulkumail.auth.models.GMessage
import fi.sulku.sulkumail.auth.models.GMessageIdList
import fi.sulku.sulkumail.auth.models.room.user.MailEntity
import fi.sulku.sulkumail.auth.models.room.user.User
import fi.sulku.sulkumail.auth.models.room.user.UserDao
import fi.sulku.sulkumail.auth.models.room.user.UserInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class UserRepository(private val userDao: UserDao) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    suspend fun removeUser(user: User) {
        userDao.deleteUser(user)
    }

    fun getUsers(): Flow<List<User>> {
        return userDao.getUsers()
    }

    fun getMails(user: User): Flow<List<MailEntity>> {
        return userDao.getInbox(user.id)
    }

    suspend fun createUser(token: Token): User {
        //todo check if user exists already
        val userInfo = fetchUserInfo(token.access_token)
        val user = User(
            userInfo = userInfo,
            token = token,
            provider = EmailProvider.GMAIL
        )
        println("Adding user: $user")
        try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            throw Exception("..")
        }
        return user
    }

    private suspend fun addMail(mailEntity: MailEntity) {
        try {
            userDao.insertEmail(mailEntity)
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return
        }
    }

    suspend fun trashMail(user: User?, mailEntity: MailEntity) {
        if (user == null) {
            throw NullPointerException("Selected user not found")
        } else {
            try {
                client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/${mailEntity.id}/trash") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                //todo better error handling Request had insufficient authentication scopes
                return
            }
            userDao.trashMail(mailEntity)
        }
    }

    //todo errohandling
    private suspend fun fetchUserInfo(token: String): UserInfo {
        val userInfo: UserInfo = client.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }.body()
        return userInfo
    }

    /*  outlook
      "error": {
    "code": 400,
    "message": "Mail service not enabled",
    "errors": [
      {
        "message": "Mail service not enabled",
        "domain": "global",
        "reason": "failedPrecondition"
      }
    ],
    "status": "FAILED_PRECONDITION"
  }
}
     */
    //todo fetch loop
    suspend fun fetchMails(user: User, folder: Folder) {
        if (_isFetching.value) {
            //todo
            return
        }
        val lastSyncTime = user.lastSyncTime
        var nextPageToken: String? = null
        val allFetchedMails = mutableListOf<MailEntity>()
        val includeSpamTrash = folder == Folder.Trash || folder == Folder.Spam
        try {
            do {
                println("fethcing mails")
                _isFetching.value = true
                val messageIdList: GMessageIdList =
                    client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                        headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                        parameter("maxResults", 10)
                        nextPageToken?.let { parameter("pageToken", it) }
                        parameter("q", "after:$lastSyncTime") // Fetch only new emails
                        parameter("labelIds", folder.label)
                        parameter("includeSpamTrash", includeSpamTrash)
                    }.body()

                val messages = messageIdList.messages

                println("a")
                if (messages.isNotEmpty()) {
                    val dupeIds = userDao.dupeIds(user.id, messages.map { it.id })
                    val uniqueMessageInfo = messages.filter { !dupeIds.contains(it.id) }
                    println("b")
                    val fetchedMails: List<MailEntity> = coroutineScope {
                        uniqueMessageInfo.map { messageInfo ->
                            async {
                                runCatching {
                                    println("fetch details ${messageInfo.id}")
                                    val mail = fetchEmailDetails(user, messageInfo.id)
                                    addMail(mail)
                                    mail
                                }.getOrNull()
                            }
                        }.awaitAll().filterNotNull()
                    }
                    println("fetched all")
                    allFetchedMails.addAll(fetchedMails) // Accumulate fetched emails
                }
                nextPageToken = messageIdList.nextPageToken // Continue fetching next page
            } while (nextPageToken != null)
        } catch (e: Exception) {
            _isFetching.value = false
            println("Exception: ${e.message}")
            return
        }
        // Update lastSyncTime only **after all pages are processed**
        if (allFetchedMails.isNotEmpty()) {
            val newLastSyncTime = allFetchedMails.maxOfOrNull { it.internalDate } ?: lastSyncTime
            user.lastSyncTime = newLastSyncTime
            userDao.updateUser(user) // Persist the final lastSyncTime
        }
        _isFetching.value = false
    }

    private suspend fun fetchEmailDetails(user: User, messageId: String): MailEntity {
        val gMessage = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMessage>()

        val senderName = gMessage.payload?.headers?.find { it.name == "From" }?.value ?: "Unknown Sender"
        val subject = gMessage.payload?.headers?.find { it.name == "Subject" }?.value ?: "No Subject"

        return MailEntity(
            id = gMessage.id,
            userId = user.id,
            sender = senderName,
            subject = subject,
            snippet = gMessage.snippet,
            internalDate = gMessage.internalDate,
            labelIds = gMessage.labelIds
        )
    }
}