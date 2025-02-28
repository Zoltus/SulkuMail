package fi.sulku.sulkumail

import kotlinx.serialization.Serializable

enum class Provider {
    GOOGLE, OUTLOOK
}

@Serializable
data class Token(
    var access_token: String,
    var expires_in: Int,
    var refresh_token: String,
    //outlook id_token?
)

@Serializable
data class TokenRequest(
    val provider: Provider,
    val code: String,
    val codeVerifier: String
)

@Serializable
data class AndroidTokenRequest(
    val provider: Provider,
    val code: String,
)

@Serializable
data class AuthResponse(
    var token: Token,
    var emailAdress: String
)



