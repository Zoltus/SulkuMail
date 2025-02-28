package fi.sulku.sulkumail.auth

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _mails = MutableStateFlow<SnapshotStateList<UnifiedEmail>>(SnapshotStateList())
    val mails = _mails.asStateFlow()


    fun setUser(user: User) {
        _user.value = user
        println("@User Set")
        //settings.encodeValue(AuthResponse.serializer(), "gtoken", authResponse)
    }

    /*
        suspend fun trashMail(unifiedMail: UnifiedEmail) {
            // todo !!
            Gmail.trashMail(_user.value!!.token, unifiedMail)
        }*/

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchMails(user: User, query: String) {
        flow<UnifiedEmail> {
            val messageIdList: GMessageIdList =
                client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                    headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                    parameter("q", query)
                    parameter("maxResults", 10)
                    //req.pageToken?.let { parameter("pageToken", req.pageToken) }
                    //req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
                }.body()
            emitAll(
                messageIdList.messages.asFlow() // Convert the list of message IDs into a flow
                    .flatMapMerge(concurrency = 10) { messageId -> // Fetch emails concurrently
                        flow {
                            val email = fetchEmailDetails(user, messageId.id)
                            emit(email)
                        }
                    }
            )
        }.collect { mail -> _mails.value.add(mail) }
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