package fi.sulku.sulkumail.auth.models.room.user


import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.EmailProvider
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val userInfo: UserInfo,
    val token: Token,
    val provider: EmailProvider
)

@Serializable
data class UserInfo(
    var displayName: String? = null,
    val name: String = "NAME_NOT_FOUND",
    val email: String = "EMAIL_NOT_FOUND",
    val photoUrl: String? = null
)