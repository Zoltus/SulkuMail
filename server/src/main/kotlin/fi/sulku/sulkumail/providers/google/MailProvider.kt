package fi.sulku.sulkumail.providers.google

import fi.sulku.sulkumail.MessageDeleteRequest
import fi.sulku.sulkumail.MessageSearchRequest
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.TokenRequest
import fi.sulku.sulkumail.UnifiedEmail
import kotlinx.coroutines.flow.Flow

sealed interface MailProvider {

    suspend fun trashMail(req: MessageDeleteRequest): UnifiedEmail

    suspend fun fetchMails(req: MessageSearchRequest): Flow<UnifiedEmail>

    suspend fun fetchToken(req: TokenRequest): Token

    suspend fun refreshToken(refreshToken: String): Token

    //suspend fun fetchEmailAdress(token: String)
    //suspend fun fetchProfilePhoto()
}
