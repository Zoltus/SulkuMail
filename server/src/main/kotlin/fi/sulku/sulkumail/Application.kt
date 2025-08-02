package fi.sulku.sulkumail

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import fi.sulku.sulkumail.providers.Google
import fi.sulku.sulkumail.server.BuildConfig
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

            //todo ratelimit
            post("/ai/summarize") { // todo lateinit/ dont recreate agent every time
                val req = call.receive<AiInputRequest>()
                val agent = AIAgent(
                    executor = simpleOllamaAIExecutor(baseUrl = BuildConfig.AI_AGENT_URL),
                    llmModel = OllamaModels.Alibaba.QWEN_3_06B,
                    systemPrompt = "You are a summarization assistant. " +
                            "Summarize the given text clearly and concisely. " +
                            "Give as a result only summary without any additional text. /no_think"
                )
                val result = agent.run(req.prompt)
                agent.close()
                call.respond(AiResponse(result.substringAfterLast("</think>")))
            }

            post("/ai/write") {
                val req = call.receive<AiInputRequest>()
                val agent = AIAgent(
                    executor = simpleOllamaAIExecutor(baseUrl = BuildConfig.AI_AGENT_URL),
                    llmModel = OllamaModels.Alibaba.QWEN_3_06B,
                    systemPrompt = "You are Email writing assistant. /no_think"
                )
                val result = agent.run(req.prompt)
                call.respond(AiResponse(result.substringAfterLast("</think>")))
                agent.close()
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
