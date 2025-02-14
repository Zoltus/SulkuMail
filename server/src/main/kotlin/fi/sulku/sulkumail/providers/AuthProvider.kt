package fi.sulku.sulkumail.providers

import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.TokenRequest

sealed interface AuthProvider {
    suspend fun fetchToken(req: TokenRequest): Token

    suspend fun refreshToken(refreshToken: String): Token
}
