package fi.sulku.sulkumail.data.auth.models.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

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
    val userId: Int, // Foreign key reference
    @PrimaryKey val id: String,
    val subject: String,
    var from: String,
    val to: List<String>,
    val snippet: String? = null,
    val date: Long,
    val htmlBody: String?,
    val plainBody: String?,
   // val attachments: List<EmailAttachment>,
    val threadId: String? = null,
    var senderImage: String? = null, // todo
    val labelIds: List<String> = emptyList(),
    //val pageToken: String? = null,
)

@Serializable
data class EmailAttachment(
    val filename: String,
    val mimeType: String,
    val size: Int,
    val attachmentId: String,
    val dataBase64: String? = null // Optional: inline base64 if available
)