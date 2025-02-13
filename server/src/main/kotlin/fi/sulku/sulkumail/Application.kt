package fi.sulku.sulkumail

import fi.sulku.sulkumail.providers.google.Google
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        Netty, port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    routing {
        route("/api") {
            //Todo providers
            //Todo flow to multiple deletes
            post("/auth") {
                val req = call.receive<TokenRequest>()
                when (req.provider) {
                    Provider.GOOGLE -> {
                        val token = Google.fetchToken(req)
                        println("Tokenb: ${token.token}")
                        call.respond(AuthResponse(token, "mailname"))
                        ServerSentEvent("")
                    }

                    Provider.OUTLOOK -> {}
                }
            }
            sse("/gmail/messages") {
                val req = call.receive<MessageSearchRequest>()
                val flow: Flow<UnifiedEmail> = Google.fetchMails(req)
                flow.collect { mail ->
                    send(ServerSentEvent(Json.encodeToString<UnifiedEmail>(UnifiedEmail.serializer(),mail)))
                }
            }
            post("/gmail/messages/trash") {
                val req = call.receive<MessageDeleteRequest>()
                val trashMessage: UnifiedEmail = Google.trashMail(req)
                call.respond(trashMessage)
            }
        }
    }
}
