package fi.sulku.sulkumail.viewmodels

import SulkuMail.shared.BuildConfig
import fi.sulku.sulkumail.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

object Gmail : MailProvider {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    override suspend fun authFlow(vm: AuthViewModel) { // todo remove vm from this?
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?response_type=code" +
                "&client_id=${BuildConfig.GOOGLE_CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.GOOGLE_REDIRECT_URL}" +
                "&scope=https://www.googleapis.com/auth/gmail.modify" +
                "&code_challenge=$codeChallenge" +
                "&code_challenge_method=S256" +
                "&access_type=offline" +
                "&prompt=consent"
        //todo state & login_hint ptional

        openUrl(authorizationUrl) { code -> // todo handle request token in callback
            val token: AuthResponse = requestToken(code, codeVerifier)
            vm.setToken(token)
        }
    }

    override suspend fun requestToken(code: String, codeVerifier: String): AuthResponse {
        val token: AuthResponse = client.post(BuildConfig.BACKEND_URL + "/api/auth") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequest(Provider.GOOGLE, code, codeVerifier))
        }.body()
        return token
    }

    suspend fun trashMail(token: Token, unifiedMail : UnifiedEmail): UnifiedEmail {
        val GMessage: GMessage =
            client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/${unifiedMail.id}/trash") {
                headers { append(HttpHeaders.Authorization, "Bearer ${token.token}") }
            }.body()
        return GMessage.toUnifiedMail()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchMails(token: Token, query : String) = flow<UnifiedEmail> {
        val messageIdList: GMessageIdList =
            client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                headers { append(HttpHeaders.Authorization, "Bearer ${token.token}") }
                parameter("q", query)
                parameter("maxResults", 10)
                //req.pageToken?.let { parameter("pageToken", req.pageToken) }
                //req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
            }.body()
        emitAll(
            messageIdList.messages.asFlow() // Convert the list of message IDs into a flow
                .flatMapMerge(concurrency = 10) { messageId -> // Fetch emails concurrently
                    flow {
                        val email = fetchEmailDetails(token, messageId.id)
                        emit(email)
                    }
                }
        )
    }

    private suspend fun fetchEmailDetails(token: Token, messageId: String): UnifiedEmail {
        val gMessage = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token.token}")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMessage>()
        return gMessage.toUnifiedMail()
    }
}

