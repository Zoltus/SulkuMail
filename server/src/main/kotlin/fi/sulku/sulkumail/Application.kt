package fi.sulku.sulkumail

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
            post("/auth") {
                val req = call.receive<TokenRequest>()
                when (req.provider) {
                    Provider.GOOGLE -> {
                        val token = gTokenRequest(req)
                        val email = gRequestEmail(token.access_token)
                        println("Tokenb: ${token.access_token}")
                        call.respond(AuthResponse(token, email))
                    }
                    Provider.OUTLOOK -> {}
                }
            }
            post("/gmail/messages") {
                val req = call.receive<MessageListRequest>()
                val messagesResp: MessageListResponse = gFetchMessageList(req)
                val messageDetails: MessagePage = gFetchEmailDetails(req.access_token, messagesResp)

                call.respond(messageDetails)
            }

            post("/gmail/messages/trash") {
                val req = call.receive<MessageDeleteRequest>()
                val trashMessage: Message = gTrashMessage(req)
                call.respond(trashMessage)
            }
        }
    }
}
