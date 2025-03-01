package fi.sulku.sulkumail.auth

import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.composables.screens.manageaccounts.EmailProvider
import kotlinx.serialization.Serializable

data class User(
    val userInfo: UserInfo,
    val token: Token,
    val provider: EmailProvider
)

@Serializable
data class UserInfo(
    var displayName: String? = null,
    val name: String = "NAME_NOT_FOUND", // todo throw exception
    val email: String = "EMAIL_NOT_FOUND",
    val photoUrl: String? = null
)