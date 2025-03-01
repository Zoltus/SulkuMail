package fi.sulku.sulkumail.composables.screens.manageaccounts

import fi.sulku.sulkumail.auth.User

sealed class AuthResult {
    class Error(val message: String) : AuthResult()
    class Success(val user: User) : AuthResult()

    inline fun onError(action: (String) -> Unit): AuthResult {
        if (this is Error) {
            action(this.message)
        }
        return this
    }

    inline fun onResult(action: (User) -> Unit): AuthResult {
        if (this is Success) {
            action(user)
        }
        return this
    }
}