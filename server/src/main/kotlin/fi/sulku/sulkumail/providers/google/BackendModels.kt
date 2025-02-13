package fi.sulku.sulkumail.providers.google

import fi.sulku.sulkumail.TokenResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    @SerialName("access_token")
    val token: String,
    @SerialName("expires_in")
    val expiresIn: Int,
) {
    fun toToken(refreshToken: String) =
        TokenResponse(token, expiresIn, refreshToken)
}