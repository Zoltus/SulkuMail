package fi.sulku.sulkumail

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
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
        json(Json { prettyPrint = true })
    }

    routing {
        route("/auth") {
            post("/register") {
            }
        }
    }
}