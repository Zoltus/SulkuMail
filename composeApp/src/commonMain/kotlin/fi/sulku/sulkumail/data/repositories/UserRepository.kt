package fi.sulku.sulkumail.data.repositories

import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.data.auth.EmailProvider
import fi.sulku.sulkumail.data.auth.models.room.User
import fi.sulku.sulkumail.data.auth.models.room.UserDao
import fi.sulku.sulkumail.data.auth.models.room.UserInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class UserRepository(private val userDao: UserDao) {

    suspend fun removeUser(user: User) {
        userDao.deleteUser(user)
    }

    fun getUsers(): Flow<List<User>> {
        return userDao.getUsers()
    }

    suspend fun createUser(token: Token): User {
        //todo check if user exists already
        val userInfo = fetchUserInfo(token.access_token)
        val user = User(
            userInfo = userInfo,
            token = token,
            provider = EmailProvider.GMAIL
        )
        println("Adding user: $user")
        try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            throw Exception("..")
        }
        return user
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    //todo errohandling
    private suspend fun fetchUserInfo(token: String): UserInfo {
        val userInfo: UserInfo = client.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }.body()
        return userInfo
    }
}