package fi.sulku.sulkumail.viewmodels

import fi.sulku.sulkumail.AuthResponse
import fi.sulku.sulkumail.Token
import kotlinx.coroutines.flow.Flow
import okio.ByteString.Companion.toByteString
import org.kotlincrypto.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed interface MailProvider {

    suspend fun authFlow(vm: AuthViewModel)

    suspend fun requestToken(code: String, codeVerifier: String): AuthResponse

    fun fetchMails(token: Token, query : String = "is:inbox") : Flow<UnifiedEmail>


    @OptIn(ExperimentalEncodingApi::class)
    fun generateCodeVerifier(): String {
        val bytes = ByteArray(32) // 32 bytes = 256 bits (recommended size)
        SecureRandom().nextBytesCopyTo(bytes)
        return Base64.UrlSafe.encode(bytes)
            .replace("=", "") // Remove padding
            .replace("+", "-")
            .replace("/", "_")
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateCodeChallenge(codeVerifier: String): String {
        val byteString = codeVerifier.encodeToByteArray().toByteString()
        val hash = byteString.sha256()
        return Base64.UrlSafe.encode(hash.toByteArray()).replace("=", "")
    }
}