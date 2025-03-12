package fi.sulku.sulkumail.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class GMessage(
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

    fun toUnifiedMail() = UnifiedEmail(
        id = id,
        sender = senderName,
        subject = subject,
        snippet = subject,
    )
}

@Serializable
data class GMessageIdList(
    val messages: List<MessageInfo>,
    val nextPageToken: String?,
    val resultSizeEstimate: Int
)

@Serializable
data class MessageInfo(
    val id: String,
    val threadId: String,
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