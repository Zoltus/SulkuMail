package fi.sulku.sulkumail

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import SulkuMail.shared.BuildConfig as SharedBuildConfig
import fi.sulku.sulkumail.server.BuildConfig as ServerBuildConfig

private val client = HttpClient {
    expectSuccess = false // To handle 4xx/5xx responses //todo
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }
}

suspend fun gFetchEmailDetails(token: String, messageIds: MessageListResponse): MessagesResp {
    val messages = messageIds.messages.mapNotNull {
        try {
             client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/${it.id}") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                parameter("format", "full")
                parameter("metadataHeaders", "Subject")
            }.body<Message>()
        } catch (e: Exception) {
            println("Failed to fetch details for message ID ${it.id}: ${e.message}")
            null
        }
    }
    println("Messages $messages")
    return MessagesResp(messageIds.nextPageToken, messages)
}

suspend fun gFetchMessageList(req: MessageListRequest): MessageListResponse =
    client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
        headers { append(HttpHeaders.Authorization, "Bearer ${req.access_token}") }
        parameter("q", req.q)
        parameter("maxResults", req.maxResults)
        req.pageToken?.let { parameter("pageToken", req.pageToken) }
        req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
    }.body()


suspend fun gTokenRequest(req: TokenRequest): Token {
    val a: Token = client.post("https://oauth2.googleapis.com/token") {
        parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
        parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
        parameter("code", req.code)
        parameter("code_verifier", req.codeVerifier)
        parameter("grant_type", "authorization_code")
        parameter("redirect_uri", SharedBuildConfig.GOOGLE_REDIRECT_URL)
    }.body()
    return a
}

suspend fun gRefreshToken(refreshToken: String): RefreshResponse {
    val a: RefreshResponse = client.post("https://oauth2.googleapis.com/token") {
        parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
        parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
        parameter("grant_type", "refresh_token")
        parameter("refresh_token", refreshToken)
    }.body()

    println("RespRefresh: $a")
    return a
}


@Serializable
data class MessageListResponse(
    val messages: List<MessageInfo>,
    val nextPageToken: String,
    val resultSizeEstimate: Int
)

@Serializable
data class MessageInfo(
    val id: String,
    val threadId: String,
)
