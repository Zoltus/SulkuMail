package fi.sulku.sulkumail.viewmodels

import SulkuMail.shared.BuildConfig
import fi.sulku.sulkumail.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

data object Gmail : MailProvider {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    override suspend fun authFlow(vm: AuthViewModel) { // todo remove vm from this?
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?response_type=code" +
                "&client_id=${BuildConfig.GOOGLE_CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.GOOGLE_REDIRECT_URL}" +
                "&scope=https://www.googleapis.com/auth/gmail.modify" +
                "&code_challenge=$codeChallenge" +
                "&code_challenge_method=S256" +
                "&access_type=offline" +
                "&prompt=consent"
        //todo state & login_hint ptional

        openUrl(authorizationUrl) { code -> // todo handle request token in callback
            val token: AuthResponse = requestToken(code, codeVerifier)
            vm.setToken(token)
        }
    }

    override suspend fun requestToken(code: String, codeVerifier: String): AuthResponse {
        val token: AuthResponse = client.post(BuildConfig.BACKEND_URL + "/api/auth") {
            contentType(ContentType.Application.Json)
            setBody(TokenRequest(Provider.GOOGLE, code, codeVerifier))
        }.body()
        return token
    }

    override suspend fun fetchEmails(token: Token, query: String, pageToken: String?): Flow<UnifiedEmail> {
        return client.post(BuildConfig.BACKEND_URL + "/api/gmail/messages") {
            contentType(ContentType.Application.Json)
            setBody(MessageSearchRequest(token = token.token))
        }.body()
    }

    override suspend fun trashMessage(token: Token, message: UnifiedEmail): UnifiedEmail =
        client.post(BuildConfig.BACKEND_URL + "/api/gmail/messages/trash") {
            contentType(ContentType.Application.Json)
            setBody(MessageDeleteRequest(token.token, message.id))
        }.body()
}

