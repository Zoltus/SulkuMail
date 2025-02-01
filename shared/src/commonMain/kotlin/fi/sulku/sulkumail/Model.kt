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
data class EmailDetail(
    val id: String,
    val subject: String,
    val snippet: String
)

@Serializable
data class EmailDetailResp(
    val pageToken: String,
    val details: List<EmailDetail>
)




enum class Provider {
    GOOGLE, OUTLOOK
}