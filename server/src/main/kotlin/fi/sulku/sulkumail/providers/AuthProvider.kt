package fi.sulku.sulkumail.providers

import fi.sulku.sulkumail.AndroidTokenRequest
import fi.sulku.sulkumail.Token

sealed interface AuthProvider {
    suspend fun exchangeAndroidCode(req: AndroidTokenRequest): Token

    suspend fun refreshToken(refreshToken: String): Token
}
