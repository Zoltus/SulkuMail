package fi.sulku.sulkumail

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
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

suspend fun gTrashMessage(req: MessageDeleteRequest) : Message =
    client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/${req.messageId}/trash") {
        headers { append(HttpHeaders.Authorization, "Bearer ${req.access_token}") }
    }.body()

//todo client or na?
suspend fun gFetchMessageList(req: MessageListRequest): MessageListResponse =
    client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
        headers { append(HttpHeaders.Authorization, "Bearer ${req.access_token}") }
        parameter("q", req.q)
        parameter("maxResults", req.maxResults)
        req.pageToken?.let { parameter("pageToken", req.pageToken) }
        req.labelIds?.let { parameter("labelIds", it.joinToString(",")) }
    }.body()

suspend fun gFetchEmailDetails(token: String, messageIds: MessageListResponse): MessagePage {
    val messages = messageIds.messages.mapNotNull {
        try {
            val message = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/${it.id}") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
                parameter("format", "full")
                parameter("metadataHeaders", "Subject")
            }.body<Message>()
            message
        } catch (e: Exception) {
            println("Failed to fetch details for message ID ${it.id}: ${e.message}")
            null
        }
    }
    return MessagePage(messageIds.nextPageToken, messages.toMutableList())
}

suspend fun gRequestEmail(accessToken: String): String {
    val response: HttpResponse = client.get("https://www.googleapis.com/gmail/v1/users/me/profile") {
        headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
    }
    val json = response.body<JsonObject>()
    return json["emailAddress"]?.jsonPrimitive?.content ?: error("Email not found")
}

suspend fun gTokenRequest(req: TokenRequest): Token {
    return client.post("https://oauth2.googleapis.com/token") {
        parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
        parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
        parameter("code", req.code)
        parameter("code_verifier", req.codeVerifier)
        parameter("grant_type", "authorization_code")
        parameter("redirect_uri", SharedBuildConfig.GOOGLE_REDIRECT_URL)
    }.body()
}

suspend fun gFetchProfilePhoto(mail: String): Token {
    val a: Token = client.get("https://admin.googleapis.com/admin/directory/v1/users/$mail/photos/thumbnail").body()
    return a
}

/*fun getUserPictureUrl(email){
    val defaultPictureUrl = 'https://lh3.googleusercontent.com/a-/AOh14Gj-cdUSUVoEge7rD5a063tQkyTDT3mripEuDZ0v=s100';
    val people = People.People.searchDirectoryPeople( {
        query: email,
        readMask: 'photos',
        sources: 'DIRECTORY_SOURCE_TYPE_DOMAIN_PROFILE'
    });
   val userPictureUrl = people?.people[0]?.photos[0]?.url;
   return userPictureUrl ?? defaultPictureUrl;
}*/

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
