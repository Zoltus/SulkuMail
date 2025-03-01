package fi.sulku.sulkumail

import fi.sulku.sulkumail.providers.Google
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
        Netty, port = 8090,
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
            post("/auth/android") {
                val req = call.receive<AndroidTokenRequest>()
                when (req.provider) {
                    Provider.GOOGLE -> {
                        val token = Google.exchangeAndroidCode(req)
                        call.respond(token)
                    }

                    Provider.OUTLOOK -> {}
                }
            }

             post("/auth/jvm") {
                val req = call.receive<TokenRequest>()
                when (req.provider) {
                    Provider.GOOGLE -> {
                        val token = Google.exchangeJvmCode(req)
                        call.respond(token)
                    }

                    Provider.OUTLOOK -> {}
                }
            }

            /*            post("/auth/refresh") {
                            val req = call.receive<RefreshRequest>()
                            when (req.provider) {
                                Provider.GOOGLE -> {
                                    val token = Google.refreshToken(req.code)
                                    println("refresh requested for: ${token.access_token}")
                                    val authResponse = AuthResponse(token, "refresh")
                                    println("@@@@@@@@@@@@@@@@@@ res: $authResponse")
                                    call.respond(AuthResponse(token, "refresh"))
                                    //ServerSentEvent("")
                                }

                                Provider.OUTLOOK -> {}
                            }
                        }*/
        }
    }
}
