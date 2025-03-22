package fi.sulku.sulkumail.auth.models.room.user


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.EmailProvider
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 1, // todo to email
    @Embedded val userInfo: UserInfo,
    @Embedded val token: Token,
    var lastSyncTime : Long = 1735680000, // Used to store the last time the user synced their emails
    val provider: EmailProvider
)

@Serializable
data class UserInfo(
    var displayName: String? = null,
    val name: String,
    val email: String,
    val photoUrl: String? = null
)