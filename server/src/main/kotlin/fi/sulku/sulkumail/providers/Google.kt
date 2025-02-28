package fi.sulku.sulkumail.providers

import fi.sulku.sulkumail.AndroidTokenRequest
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.TokenRequest
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import SulkuMail.shared.BuildConfig as SharedBuildConfig
import fi.sulku.sulkumail.server.BuildConfig as ServerBuildConfig

//todo error handling
object Google : AuthProvider {
    private val client = HttpClient {
        expectSuccess = false // To handle 4xx/5xx responses //todo
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    override suspend fun exchangeAndroidCode(req: AndroidTokenRequest): Token {
        return client.post("https://oauth2.googleapis.com/token") {
            parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
            parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
            parameter("code", req.code)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", SharedBuildConfig.GOOGLE_REDIRECT_URL)
            parameter("access_type", "offline")
        }.body()
    }

    suspend fun exchangeJvmCode(req: TokenRequest): Token {
        return client.post("https://oauth2.googleapis.com/token") {
            parameter("client_id", SharedBuildConfig.GOOGLE_CLIENT_ID)
            parameter("client_secret", ServerBuildConfig.GOOGLE_API_SECRET)
            parameter("code", req.code)
            parameter("code_verifier", req.codeVerifier)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", SharedBuildConfig.GOOGLE_REDIRECT_URL)
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): Token {
        TODO("Not yet implemented")
    }

    /*    override suspend fun refreshToken(refreshToken: String): Token {
            val refreshResp = client.post("https://oauth2.googleapis.com/token") {
                parameter("client_id", "831176601060-rk47ejal0k9me43en2l2q2he21ba47s7.apps.googleusercontent.com")
                parameter("client_secret", "AIzaSyD8Pxn_UhkxeO6YlVT4W4LxDcDHQG8Urv4")
                parameter("grant_type", "refresh_token")
                parameter("refresh_token", refreshToken)
            }.bodyAsText()
            println("Refresh done: $refreshResp")
            val t = Json.decodeFromString<RefreshResponse>(refreshResp)
            return t.toToken(refreshToken)
        }*/
    /*
    suspend fun gRequestEmail(accessToken: String): String {
    val response: HttpResponse = client.get("https://www.googleapis.com/gmail/v1/users/me/profile") {
        headers { append(HttpHeaders.Authorization, "Bearer $accessToken") }
    }
    val json = response.body<JsonObject>()
    return json["emailAddress"]?.jsonPrimitive?.content ?: error("Email not found")
}
     */


}