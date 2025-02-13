package fi.sulku.sulkumail.providers.google

import fi.sulku.sulkumail.TokenResponse
import fi.sulku.sulkumail.UnifiedEmail
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import SulkuMail.shared.BuildConfig as SharedBuildConfig
import fi.sulku.sulkumail.server.BuildConfig as ServerBuildConfig

//todo error handling
object Google : MailProvider {
    private val client = HttpClient {
        expectSuccess = false // To handle 4xx/5xx responses //todo
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    override suspend fun trashMail(token: String, mailId: String): UnifiedEmail {
        val gMail: GMail =
            client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/$mailId/trash") {
                headers { append(HttpHeaders.Authorization, "Bearer $token") }
            }.body()
        return gMail.toUnifiedMail()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun fetchMails(token: String, query: String) = flow<UnifiedEmail> {
        val messageIdList: GMessageIdList =
            client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                headers { append(HttpHeaders.Authorization, "Bearer $token") }
                parameter("q", query)
                parameter("maxResults", 5)
                // req.pageToken?.let { parameter("pageToken", req.pageToken) }
                //  req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
            }.body()

        emitAll(
            messageIdList.messages.asFlow() // Convert the list of message IDs into a flow
                .flatMapMerge(concurrency = 5) { messageId -> // Fetch emails concurrently (adjust concurrency level)
                    flow {
                        val email = fetchEmailDetails(token, messageId.id)
                        emit(email) // Emit each email as it's fetched
                    }
                }
        )
    }

    private suspend fun fetchEmailDetails(token: String, messageId: String): UnifiedEmail {
        val gMail = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMail>()
        return gMail.toUnifiedMail()
    }

    override suspend fun fetchToken(code: String, codeVerifier: String): TokenResponse {
        return client.post("https://oauth2.googleapis.com/token") {
            parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
            parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
            parameter("code", code)
            parameter("code_verifier", codeVerifier)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", SharedBuildConfig.GOOGLE_REDIRECT_URL)
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): TokenResponse {
        val refreshResp: RefreshResponse = client.post("https://oauth2.googleapis.com/token") {
            parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
            parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
            parameter("grant_type", "refresh_token")
            parameter("refresh_token", refreshToken)
        }.body()
        return refreshResp.toToken(refreshToken)
    }
    /*
    suspend fun gRequestEmail(accessToken: String): String {
    val response: HttpResponse = client.get("https://www.googleapis.com/gmail/v1/users/me/profile") {
        headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
    }
    val json = response.body<JsonObject>()
    return json["emailAddress"]?.jsonPrimitive?.content ?: error("Email not found")
}
     */


}