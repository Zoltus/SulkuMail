package fi.sulku.sulkumail.data.auth

import fi.sulku.sulkumail.data.auth.models.room.User
import fi.sulku.sulkumail.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(private val userRepo: UserRepository) {

    val users = userRepo.getUsers()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    fun selectUser(user: User) {
        println("selected user")
        _selectedUser.value = user
    }

    suspend fun removeUser(user: User) = userRepo.removeUser(user)
}