package fi.sulku.sulkumail.data.auth.models.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "emails",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class MailEntity(
    @PrimaryKey val id: String,
    val userId: Int, // Foreign key reference
    var sender: String? = null,
    val subject: String? = null,
    val snippet: String? = null,
    var senderImage: String? = null,
    val pageToken: String? = null,
    val internalDate: Long,
    val labelIds: List<String>? = null,
)