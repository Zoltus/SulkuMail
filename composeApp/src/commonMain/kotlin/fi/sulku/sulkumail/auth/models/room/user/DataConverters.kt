package fi.sulku.sulkumail.auth.models.room.user

import androidx.room.TypeConverter
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.EmailProvider
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromUserInfo(userInfo: UserInfo): String {
        return json.encodeToString(userInfo)
    }

    @TypeConverter
    fun toUserInfo(data: String): UserInfo {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromToken(token: Token): String {
        return json.encodeToString(token)
    }

    @TypeConverter
    fun toToken(data: String): Token {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromEmailProvider(provider: EmailProvider): String {
        return provider.name // Store as a string
    }

    @TypeConverter
    fun toEmailProvider(name: String): EmailProvider {
        return EmailProvider.valueOf(name)
    }
}
