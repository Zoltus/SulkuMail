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
    val name: String,
    val email: String,
    val picture: String?
)