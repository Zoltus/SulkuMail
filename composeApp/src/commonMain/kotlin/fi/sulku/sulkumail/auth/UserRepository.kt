package fi.sulku.sulkumail.auth

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.russhwolf.settings.Settings
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class UserRepository(private val settings: Settings) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    // todo authvm has access to edit atm, figure out a stateflow
    val users: SnapshotStateList<User> = mutableStateListOf()
    //todo val users: List<User> = _users

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

     fun selectUser(user: User) {
        println("User Selected")
        _selectedUser.value = user
    }

    // <userUUID, <mailId, mail>
    private val _mailsByUser: SnapshotStateMap<String, SnapshotStateMap<String, UnifiedEmail>> = mutableStateMapOf()

    fun getMails(user: User): SnapshotStateMap<String, UnifiedEmail>? {
        val uuid = user.uuid
        val mails = _mailsByUser[uuid]
        return mails
    }

    suspend fun createUser(token: Token): User {
        //todo check if user exists already
        val userInfo = fetchUserInfo(token.access_token)
        val user = User(userInfo, token, EmailProvider.GMAIL)
        users.add(user)
        println("@User added")
        //settings.encodeValue(AuthResponse.serializer(), "gtoken", authResponse)
        return user
    }

    private fun addMail(user: User, unifiedEmail: UnifiedEmail) {
        val uuid = user.uuid
        val mails = _mailsByUser.getOrPut(uuid) { mutableStateMapOf() }
        mails[unifiedEmail.id] = unifiedEmail
    }

    suspend fun trashMail(user: User?, unifiedMail: UnifiedEmail): UnifiedEmail? {
        if (user == null) {
            throw NullPointerException("Selected user not found")
        } else {
            val uuid = user.uuid
            val mails = _mailsByUser.getOrPut(uuid) { mutableStateMapOf() }

            val gMessage: GMessage = try {
                client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/${unifiedMail.id}/trash") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                }.body()
            } catch (e: Exception) {
                // Return null if it fails to delete mail, todo better error handling
                return null
            }
            mails.remove(unifiedMail.id)
            return gMessage.toUnifiedMail()
        }
    }

    //todo errohandling
    private suspend fun fetchUserInfo(token: String): UserInfo {
        val userInfo: UserInfo = client.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }.body()
        return userInfo
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchMails(user: User, query: String = "") {
        val messageIdList: GMessageIdList =
            client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                parameter("q", query)
                parameter("maxResults", 10)
                //req.pageToken?.let { parameter("pageToken", req.pageToken) }
                //req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
            }.body()

        val uuid = user.uuid
        val mailList = _mailsByUser[uuid]
        val uniqueMessages = messageIdList.messages.filter { info ->
            mailList?.contains(info.id) != true
        }

        // Fetch message details for all unique mails
        coroutineScope {
            uniqueMessages.map { messageInfo ->
                async {
                    runCatching { // todo better error handling
                        val unifiedMail = fetchEmailDetails(user, messageInfo.id)
                        addMail(user, unifiedMail)
                    }
                }
            }.awaitAll()
        }
    }

    private suspend fun fetchEmailDetails(user: User, messageId: String): UnifiedEmail {
        val gMessage = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMessage>()
        return gMessage.toUnifiedMail()
    }
}