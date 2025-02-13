package fi.sulku.sulkumail

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val provider: Provider,
    val code: String,
    val codeVerifier: String
)

@Serializable
data class AuthResponse(
    var token: Token,
    var emailAdress: String
)

@Serializable
data class Token(
    var access_token: String,
    var expires_in: Int,
    var refresh_token: String,
)

@Serializable
data class RefreshResponse( // Todo can Token be used?
    val access_token: String,
    val expires_in: Int,
)

@Serializable
data class MessageListRequest(
    val access_token: String,
    val maxResults: Int = 20, // 1-500
    val pageToken: Int? = null,
    val q: String = "is:inbox", //Search query is:inbox
    val labelIds: List<String>? = null
)

@Serializable
data class MessageDeleteRequest(
    val access_token: String,
    val messageId: String,
)

@Serializable
data class MessagePage(
    val pageToken: String? = null,
    val messages: List<Message> = emptyList(),
)

@Serializable
data class Message(
    val id: String,
    val threadID: String? = null,
    val labelIds: List<String>? = null,
    val snippet: String? = null,
    val internalDate: String? = null,
    val payload: MessagePart? = null,
    var senderImage: String? = null
) {
    val senderName = payload?.headers?.find { it.name == "From" }?.value ?: "Unknown Sender"
    val subject = payload?.headers?.find { it.name == "Subject" }?.value ?: "No Subject"
}

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