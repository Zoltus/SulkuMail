package fi.sulku.sulkumail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class Provider {
    GOOGLE, OUTLOOK
}

@Serializable
data class Token(
    @SerialName("access_token")
    var token: String,
    @SerialName("expires_in")
    var expiresIn: Int,
    @SerialName("refresh_token")
    var refreshToken: String,
    //outlook id_token?
)

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



