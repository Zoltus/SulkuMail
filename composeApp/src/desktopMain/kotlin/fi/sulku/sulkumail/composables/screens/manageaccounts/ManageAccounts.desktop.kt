package fi.sulku.sulkumail.composables.screens.manageaccounts

import SulkuMail.shared.BuildConfig
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.AuthResponse
import fi.sulku.sulkumail.Provider
import fi.sulku.sulkumail.TokenRequest
import fi.sulku.sulkumail.auth.UserViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.toByteString
import org.koin.compose.viewmodel.koinViewModel
import org.kotlincrypto.SecureRandom
import java.awt.Desktop
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
actual fun PlatformGoogleLogin(
    scopes: List<String>,
    onAuthResponse: (AuthResponse) -> Unit
) {
    val authVm: UserViewModel = koinViewModel<UserViewModel>()

    authVm.viewModelScope.launch {
        val startAuthFlow = startAuthFlow(scopes)
        onAuthResponse(startAuthFlow)
    }
}


suspend fun startAuthFlow(scopes: List<String>): AuthResponse {
    val authUrl = "https://accounts.google.com/o/oauth2/v2/auth"
    val scopeString = scopes.joinToString(" ")
    val encodedScope = URLEncoder.encode(scopeString, StandardCharsets.UTF_8.toString())
    val state = generateCodeRandomString()
    val nonce = generateCodeRandomString()

    val codeVerifier = generateCodeRandomString()
    val codeChallenge = generateCodeChallenge(codeVerifier)

    val googleAuthUrl = "$authUrl?" +
            "client_id=${BuildConfig.GOOGLE_CLIENT_ID}" +
            "&redirect_uri=${BuildConfig.GOOGLE_REDIRECT_URL}" +
            "&response_type=code" +
            "&scope=$encodedScope" +
            "&nonce=$nonce" +
            "&state=$state" +
            "&code_challenge=$codeChallenge" +
            "&code_challenge_method=S256" +
            "&access_type=offline" +
            "&prompt=consent"

    openUrlInBrowser(googleAuthUrl)
    val code = startLocalServerForCode(state = state)

    if (code != null) {
        return exchangeCodeForToken(code = code, codeVerifier = codeVerifier)
    } else {
        throw Exception("Temp Auth Exception")
        println("Authorization failed")
    }
}

private fun openUrlInBrowser(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
    } else {
        println("GoogleAuthUiProvider: Desktop is not supported on this platform.")
    }
}

private suspend fun startLocalServerForCode(state: String): String? {
    val codeDeferred = CompletableDeferred<String?>()

    val server = embeddedServer(Netty, port = 8079) {
        routing {
            get("/callback") {
                val code = call.request.queryParameters["code"]
                val returnedState = call.request.queryParameters["state"]

                // Verify state to prevent CSRF attacks
                if (!code.isNullOrEmpty() && returnedState == state) {
                    call.respondText(
                        "Authorization successful! You can close this window and return to the application.",
                        contentType = ContentType.Text.Plain
                    )
                    codeDeferred.complete(code)
                } else {
                    call.respondText(
                        "Authorization failed",
                        contentType = ContentType.Text.Plain
                    )
                    codeDeferred.complete(null)
                }
            }
        }
    }.start(wait = false)

    val authCode = codeDeferred.await()
    server.stop(1000, 1000)
    return authCode
}

private suspend fun exchangeCodeForToken(code: String, codeVerifier: String): AuthResponse {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }.use { client ->
        client.post(BuildConfig.BACKEND_URL + "/api/auth/jvm") {
            contentType(ContentType.Application.Json)
            setBody(
                TokenRequest(
                    Provider.GOOGLE,
                    code,
                    codeVerifier
                )
            )
        }.body()
    }
}

@OptIn(ExperimentalEncodingApi::class)
private fun generateCodeRandomString(): String {
    val bytes = ByteArray(32)
    SecureRandom().nextBytesCopyTo(bytes)
    return Base64.UrlSafe.encode(bytes).replace("=", "")
}

@OptIn(ExperimentalEncodingApi::class)
private fun generateCodeChallenge(codeVerifier: String): String {
    val byteString = codeVerifier.encodeToByteArray().toByteString()
    val hash = byteString.sha256()
    return Base64.UrlSafe.encode(hash.toByteArray()).replace("=", "")
}