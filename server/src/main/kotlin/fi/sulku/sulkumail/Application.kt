package fi.sulku.sulkumail

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

fun main() {
    embeddedServer(
        Netty, port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

val dotenv = dotenv()
val clientId: String = dotenv["DISCORD_CLIENT_ID"]
val clientSecret: String = dotenv["DISCORD_CLIENT_SECRET"]
val redirectUri: String = dotenv["DISCORD_REDIRECT_URI"]

val userSessions = ConcurrentHashMap<String, DefaultWebSocketSession>()

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    install(WebSockets) {
        pingPeriod = 10.toDuration(DurationUnit.MINUTES).toJavaDuration()
    }

    routing {
        webSocket("/ws/{userId}") {
            val userId =
                call.parameters["userId"] ?: return@webSocket close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "No userId"))
            userSessions[userId] = this

            try {
                //Catch frames sent by user
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                       val receivedText = frame.readText()
                        this.send("Echo: $receivedText")
                    }
                }
            } finally {
                userSessions.remove(userId)
                log.error("\nUser $userId disconnected")
            }
        }

        get("/") {
            call.respondText("Ktor: ")
        }

        get("/auth/discord") {
            val discordAuthUrl =
                "https://discord.com/oauth2/authorize?client_id=$clientId&response_type=code&redirect_uri=$redirectUri&scope=identify+email"
            call.respondRedirect(discordAuthUrl)
        }

        get("/auth/discord/callback") {
            log.info("\n\na@@@@@@@@@@@@@@@@")
            //todo check "verified":true so discout account is verified
            val code = call.request.queryParameters["code"]
            if (code != null) {
                val fetchAccessToken: AccessTokenResponse = fetchAccessToken(code)
                log.info("\n\nfetchAccessToken : $fetchAccessToken")
                val userData: String = getUserData(fetchAccessToken) // todo model for this
                val data = toUserData(userData)
                //todo temp test send to all
                GlobalScope.launch {
                    for (userSession in userSessions.values) {
                        userSession.send(Json.encodeToString(data))
                    }
                }
                log.info("\n\n222@@@@@@@@@@@Logged In")
                call.respond(data)

            } else {
                call.respondText("Authorization failed", status = HttpStatusCode.BadRequest)
                log.info("\n\n333@@@@@@@@@@@@@@@@")
            }
            client.close()
        }

        post("") {

        }
    }
}

//todo move to clientside
val json = Json { ignoreUnknownKeys = true }

val client = HttpClient {
    install(ContentNegotiation) { json }
}
//client.close()
//https://github.com/Gameoholic/Fancy2FA/blob/ad2257ae499a8aa6915a9b9c528278794d06d7a6/src/main/kotlin/com/github/gameoholic/fancy2fa/discord/DiscordAutentication.kt#L52
//https://github.com/magonxesp/usada-pekora-experiment/blob/de40b28bca3092cbeb37c68cd2af1a721c4c09d2/contexts/auth/src/main/kotlin/com/usadapekora/auth/infrastructure/oauth/discord/DiscordOAuthProvider.kt#L43


suspend fun getUserData(accessToken: AccessTokenResponse): String {
    /*    val response1: HttpResponse = client.get("https://discordapp.com/api/users/@me") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }*/
    val response = client.get("https://discord.com/api/v10/users/@me") {
        header("Authorization", "Bearer ${accessToken.access_token}")
    }
    return response.bodyAsText()
}

fun toUserData(jsonString: String): UserData {
    return json.decodeFromString(jsonString)
}

suspend fun fetchAccessToken(code: String): AccessTokenResponse {
    val url = "https://discord.com/api/v10/oauth2/token"
    val clientId = clientId
    val clientSecret = clientSecret

    val response: HttpResponse = client.submitForm(
        url = url,
        formParameters = Parameters.build {
            append("client_id", clientId)
            append("client_secret", clientSecret)
            append("grant_type", "authorization_code")
            append("code", code)
            append("redirect_uri", redirectUri)
        }
    )
    println("\n\nresponse : ${response.bodyAsText()}")
    return json.decodeFromString(response.bodyAsText())
}

@Serializable
data class AccessTokenResponse(
    val token_type: String,
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String
)

