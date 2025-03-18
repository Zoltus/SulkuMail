package fi.sulku.sulkumail.auth

import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.models.GMessage
import fi.sulku.sulkumail.auth.models.GMessageIdList
import fi.sulku.sulkumail.auth.models.MessageInfo
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

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    fun getUsers(): Flow<List<User>> {
        return userDao.getUsers()
    }

    fun getMails(user: User): Flow<List<MailEntity>> {
        return userDao.getMails(user.id)
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
            userDao.deleteEmail(mailEntity)
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
    suspend fun fetchMails(user: User, query: String = "") {
        val messageIdList : GMessageIdList =
            client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                parameter("q", query)
                parameter("maxResults", 10)
                //req.pageToken?.let { parameter("pageToken", req.pageToken) }
                //req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
            }.body()

        val messages: MutableList<MessageInfo> = messageIdList.messages.toMutableList()
        //Unique messages which arent in database
        val dupeIds = userDao.dupeIds(user.id, messages.map { it.id })
        val uniqueMessageInfo = messages.filter { !dupeIds.contains(it.id) }

        // Fetch message details for all unique mails
        coroutineScope {
            uniqueMessageInfo.map { messageInfo ->
                async {
                    runCatching { // todo better error handling
                        val unifiedMail = fetchEmailDetails(user, messageInfo.id)
                        addMail(unifiedMail)
                    }
                }
            }.awaitAll()
        }
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
        )
    }
}