package fi.sulku.sulkumail

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        Netty, port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    routing {
        post("/auth") {
            val req = call.receive<TokenRequest>()
            when (req.provider) {
                Provider.GOOGLE -> {
                    call.respond(gTokenRequest(req))
                }
                Provider.OUTLOOK -> {}
            }
        }
        post("/messages") {
            val req = call.receive<MessageListRequest>()
            val messagesResp: MessageListResponse = gFetchMessageList(req)
            val messageDetails: MessagesResp = gFetchEmailDetails(req.access_token, messagesResp)
            call.respond(messageDetails)
        }
    }
}
