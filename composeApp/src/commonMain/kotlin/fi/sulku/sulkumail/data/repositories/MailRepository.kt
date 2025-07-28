package fi.sulku.sulkumail.data.repositories

import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.models.GMessage
import fi.sulku.sulkumail.data.auth.models.GMessageIdList
import fi.sulku.sulkumail.data.auth.models.MessagePart
import fi.sulku.sulkumail.data.auth.models.room.EmailAttachment
import fi.sulku.sulkumail.data.auth.models.room.MailEntity
import fi.sulku.sulkumail.data.auth.models.room.User
import fi.sulku.sulkumail.data.auth.models.room.UserDao
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class MailRepository(private val userDao: UserDao) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    fun getMails(user: User): Flow<List<MailEntity>> {
        return userDao.getInbox(user.id)
    }

    fun getMail(user: User, mailId: String): Flow<MailEntity?> {
        return userDao.getMailById(user.id, mailId)
    }

    private suspend fun addMail(mailEntity: MailEntity) {
        try {
            userDao.insertEmail(mailEntity)
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return
        }
    }

    suspend fun trashMail(user: User, mailEntity: MailEntity) {
        try {
            client.post("https://gmail.googleapis.com/gmail/v1/users/me/messages/${mailEntity.id}/trash") {
                headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            //todo better error handling Request had insufficient authentication scopes
            return
        }
        userDao.trashMail(mailEntity)
    }

    /*  outlook
      "error": {
    "code": 400,
    "message": "Mail service not enabled",
    "errors": [
      {
        "message": "Mail service not enabled",
        "domain": "global",
        "reason": "failedPrecondition"
      }
    ],
    "status": "FAILED_PRECONDITION"
  }
}
     */
    //todo fetch loop
    suspend fun fetchMails(user: User, folder: Folder) {
        val lastSyncTime = user.lastSyncTime
        var nextPageToken: String? = null
        val allFetchedMails = mutableListOf<MailEntity>()
        val includeSpamTrash = folder == Folder.Trash || folder == Folder.Spam
        try {
            do {
                println("fethcing mails")
                val messageIdList: GMessageIdList =
                    client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages") {
                        headers { append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}") }
                        parameter("maxResults", 10)
                        nextPageToken?.let { parameter("pageToken", it) }
                        parameter("q", "after:$lastSyncTime") // Fetch only new emails
                        parameter("labelIds", folder.label)
                        parameter("includeSpamTrash", includeSpamTrash)
                    }.body()

                val messages = messageIdList.messages

                println("a")
                if (messages.isNotEmpty()) {
                    val dupeIds = userDao.dupeIds(user.id, messages.map { it.id })
                    val uniqueMessageInfo = messages.filter { !dupeIds.contains(it.id) }
                    println("b")
                    val fetchedMails: List<MailEntity> = coroutineScope {
                        uniqueMessageInfo.map { messageInfo ->
                            async {
                                runCatching {
                                    println("fetch details ${messageInfo.id}")
                                    val mail = fetchEmailDetails(user, messageInfo.id)
                                    addMail(mail)
                                    mail
                                }.getOrNull()
                            }
                        }.awaitAll().filterNotNull()
                    }
                    println("fetched all")
                    allFetchedMails.addAll(fetchedMails) // Accumulate fetched emails
                }
                nextPageToken = messageIdList.nextPageToken // Continue fetching next page
            } while (nextPageToken != null)
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return
        }
        // Update lastSyncTime only **after all pages are processed**
        if (allFetchedMails.isNotEmpty()) {
            val newLastSyncTime = allFetchedMails.maxOfOrNull { it.date } ?: lastSyncTime
            user.lastSyncTime = newLastSyncTime
            userDao.updateUser(user) // Persist the final lastSyncTime
        }
    }

    private suspend fun fetchEmailDetails(user: User, messageId: String): MailEntity {
        val g = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMessage>()

        val subject = g.payload?.headers?.find { it.name.equals("Subject", true) }?.value ?: "No Subject"
        val from = g.payload?.headers?.find { it.name.equals("From", true) }?.value ?: "Unknown Sender"
        val to = g.payload?.headers?.filter { it.name.equals("To", true) }?.map { it.value } ?: emptyList()

        val htmlBody = findMimeBody(g.payload, "text/html")
        val plainBody = findMimeBody(g.payload, "text/plain")
        val attachments = extractAttachments(g.payload)

        return MailEntity(
            userId = user.id,
            id = g.id,
            subject = subject,
            from = from,
            to = to,
            snippet = g.snippet,
            date = g.internalDate,
            htmlBody64 = htmlBody,
            plainBody64 = plainBody,
            //attachments = attachments,
            threadId = g.threadID,
            labelIds = g.labelIds ?: emptyList()
        )
    }

    private fun findMimeBody(part: MessagePart?, targetMimeType: String): String? {
        if (part == null) return null

        // Check if this part matches the desired MIME type
        if (part.mimeType.equals(targetMimeType, ignoreCase = true) && part.body?.data != null) {
            return part.body.data
        }

        // If this part has subparts (multipart/alternative, etc), recurse
        part.parts?.forEach { subPart ->
            val found = findMimeBody(subPart, targetMimeType)
            if (found != null) return found
        }

        return null
    }


    private fun extractAttachments(payload: MessagePart?): List<EmailAttachment> {
        val attachments = mutableListOf<EmailAttachment>()

        fun recurse(part: MessagePart?) {
            if (part == null) return

            if (part.filename.isNotBlank() && part.body?.attachmentId != null) {
                attachments.add(
                    EmailAttachment(
                        filename = part.filename,
                        mimeType = part.mimeType,
                        size = part.body.size,
                        attachmentId = part.body.attachmentId,
                        dataBase64 = part.body.data // Sometimes Gmail inlines small attachments
                    )
                )
            }

            part.parts?.forEach { recurse(it) }
        }

        recurse(payload)
        return attachments
    }

}