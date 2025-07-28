package fi.sulku.sulkumail.data.auth.models.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
    val htmlBody64: String?,
    val plainBody64: String?, // todo fallback
    // val attachments: List<EmailAttachment>,
    val threadId: String? = null,
    var senderImage: String? = null, // todo
    val labelIds: List<String> = emptyList(),
    //val pageToken: String? = null,
) {
    fun getDecodedHtmlBody(): String? {
        return htmlBody64?.let { decodeBase64UrlSafe(it) }
    }

    fun getDecodedPlainBody(): String? {
        return plainBody64?.let { decodeBase64UrlSafe(it) }
    }

    @OptIn(ExperimentalEncodingApi::class) // todo
    private fun decodeBase64UrlSafe(data: String): String {
        return try {
            // Convert URL-safe Base64 to standard Base64
            val standardBase64 = data
                .replace('-', '+')
                .replace('_', '/')
                .let { base64String ->
                    val padding = (4 - base64String.length % 4) % 4
                    base64String + "=".repeat(padding)
                }

            val decoded = Base64.Default.decode(standardBase64)
            decoded.decodeToString()
        } catch (e: Exception) {
            println("Base64 decode error: ${e.message}")
            ""
        }
    }
}

@Serializable
data class EmailAttachment(
    val filename: String,
    val mimeType: String,
    val size: Int,
    val attachmentId: String,
    val dataBase64: String? = null // Optional: inline base64 if available
)