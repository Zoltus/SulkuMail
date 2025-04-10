package fi.sulku.sulkumail.data.repositories

import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.models.GMessage
import fi.sulku.sulkumail.data.auth.models.GMessageIdList
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
            val newLastSyncTime = allFetchedMails.maxOfOrNull { it.internalDate } ?: lastSyncTime
            user.lastSyncTime = newLastSyncTime
            userDao.updateUser(user) // Persist the final lastSyncTime
        }
    }

    private suspend fun fetchEmailDetails(user: User, messageId: String): MailEntity {
        val gMessage = client.get("https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${user.token.access_token}")
            }
            parameter("format", "full")
            parameter("metadataHeaders", "Subject")
        }.body<GMessage>()

        val senderName = gMessage.payload?.headers?.find { it.name == "From" }?.value ?: "Unknown Sender"
        val subject = gMessage.payload?.headers?.find { it.name == "Subject" }?.value ?: "No Subject"

        return MailEntity(
            id = gMessage.id,
            userId = user.id,
            sender = senderName,
            subject = subject,
            snippet = gMessage.snippet,
            internalDate = gMessage.internalDate,
            labelIds = gMessage.labelIds
        )
    }
}