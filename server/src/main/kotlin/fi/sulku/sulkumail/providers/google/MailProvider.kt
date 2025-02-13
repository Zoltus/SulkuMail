package fi.sulku.sulkumail.providers.google

import fi.sulku.sulkumail.TokenResponse
import fi.sulku.sulkumail.UnifiedEmail
import kotlinx.coroutines.flow.Flow

sealed interface MailProvider {

    suspend fun trashMail(token: String, mailId: String): UnifiedEmail

    suspend fun fetchMails(token: String, query: String): Flow<UnifiedEmail>

    suspend fun fetchToken(code: String, verifier: String): TokenResponse

    suspend fun refreshToken(refreshToken: String) : TokenResponse

    //suspend fun fetchEmailAdress(token: String)
    //suspend fun fetchProfilePhoto()
}
