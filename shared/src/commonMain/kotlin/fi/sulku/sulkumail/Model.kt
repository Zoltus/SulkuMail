package fi.sulku.sulkumail

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val provider: Provider,
    val code: String,
    val codeVerifier: String
)

@Serializable
data class Token(
    var access_token: String,
    val expires_in: Int,
    val refresh_token: String
)

@Serializable
data class RefreshResponse(
    val access_token: String,
    val expires_in: Int,
)

@Serializable
data class MessageListRequest(
    val access_token: String,
    val maxResults: Int = 10, // 1-500
    val pageToken: Int? = null,
    val q: String = "is:inbox", //Search query is:inbox
    val labelIds: List<String>? = null
)

@Serializable
data class MessagesResp(
    val pageToken: String?,
    val messages: List<Message>,
)

@Serializable
data class Message(
    val id: String,
    val threadID: String? = null,
    val labelIds: List<String>? = null,
    val snippet: String,
    val internalDate: String,
    val payload: MessagePart,
)

@Serializable
data class MessagePart(
    val partId: String,
    val mimeType: String,
    val filename: String,
    val headers: List<Header>,
    val body: MessagePartBody?,
    val parts: List<MessagePart>? = null
)

@Serializable
data class Header(
    val name: String,
    val value: String,
)

@Serializable
data class MessagePartBody(
    val attachmentId: String? = null,
    val size: Int,
    val data: String? = null,
)


enum class Provider {
    GOOGLE, OUTLOOK
}