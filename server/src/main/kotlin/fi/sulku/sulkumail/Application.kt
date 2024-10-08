package fi.sulku.sulkumail

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.java.*
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

fun Application.module() {

    routing {
        get("/") {
            call.respondText("Ktor: ")
        }

        get("/auth/discord") {
            val discordAuthUrl =
                "https://discord.com/oauth2/authorize?client_id=$clientId&response_type=code&redirect_uri=$redirectUri&scope=identify+email"
            call.respondRedirect(discordAuthUrl)
        }

        post("") {

        }

        get("/auth/discord/callback") {
            //todo check "verified":true so discout account is verified
            val code = call.request.queryParameters["code"]
            if (code != null) {
                val fetchAccessToken: AccessTokenResponse = fetchAccessToken(code)
                println("\n\nfetchAccessToken : $fetchAccessToken")
                val userData: String = getUserData(fetchAccessToken) // todo model for this
                val data = toUserData(userData)
                call.respond(data)
            } else {
                call.respondText("Authorization failed", status = HttpStatusCode.BadRequest)
            }
            client.close()
        }
    }
}

//todo move to clientside
val json = Json { ignoreUnknownKeys = true }

val client = HttpClient(Java) {
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

