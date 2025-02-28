package fi.sulku.sulkumail.composables.screens.manageaccounts

import fi.sulku.sulkumail.Token

data class User(
    val name: String,
    val displayName: String = "",
    val email: String? = null,
    val photoUrl: String? = null,
    val token: Token
)