package fi.sulku.sulkumail.auth.models


import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.EmailProvider
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class User(
    val userInfo: UserInfo,
    val token: Token,
    val provider: EmailProvider,
    val mails: List<UnifiedEmail> = emptyList() // For storing
) {
    @OptIn(ExperimentalUuidApi::class)
    val uuid: String = Uuid.random().toString()
}

@Serializable
data class UserInfo(
    var displayName: String? = null,
    val name: String = "NAME_NOT_FOUND", // todo throw exception
    val email: String = "EMAIL_NOT_FOUND",
    val photoUrl: String? = null
)