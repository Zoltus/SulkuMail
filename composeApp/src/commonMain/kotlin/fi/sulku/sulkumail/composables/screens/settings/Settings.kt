package fi.sulku.sulkumail.composables.screens.settings

import SulkuMail.shared.BuildConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.*
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.toByteString
import org.koin.compose.viewmodel.koinViewModel
import org.kotlincrypto.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun Settings() {
    val authVm = koinViewModel<AuthViewModel>()
    val codeVerifier = generateCodeVerifier()
    val codeChallenge = generateCodeChallenge(codeVerifier)
    val authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
            "?response_type=code" +
            "&client_id=${BuildConfig.GOOGLE_CLIENT_ID}" +
            "&redirect_uri=${BuildConfig.GOOGLE_REDIRECT_URL}" +
            "&scope=https://www.googleapis.com/auth/gmail.readonly" +
            "&code_challenge=$codeChallenge" +
            "&code_challenge_method=S256" +
            "&access_type=offline" +
            "&prompt=consent"
    //todo state
    //login_hint	Optional
    val token by authVm.token.collectAsState()
    val emailDetails by authVm.messagePage.collectAsState()

    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Text(
            fontSize = 30.sp,
            text = "Settings"
        )
        Spacer(Modifier.height(50.dp))
        Button(onClick = {
            authVm.viewModelScope.launch { //todo wrong scope
                openUrl(authorizationUrl) { code -> // todo handle request token in callback
                    authVm.setToken(requestToken(code, codeVerifier))
                }
            }
        }) {
            Text("add gmail")
        }

        token?.let {
            Button(onClick = {
                authVm.viewModelScope.launch { //todo wrong scope
                    val messagePage = requestMessagePage(token!!) // todo temp!!
                    authVm.setMessagePage(messagePage)
                    //println(messagePage)
                }
            }) {
                Text("GetMessageList")
            }
        }

        Button(onClick = {
            println("vall ${authVm.messagePage.value}")
            println("wholedetails $emailDetails")
        }) {
            Text("getStoredEmailsviewModel")
        }
    }
}

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }
}

suspend fun requestToken(code: String, codeVerifier: String): Token =
    client.post(BuildConfig.BACKEND_URL + "/api/auth") {
        contentType(ContentType.Application.Json)
        setBody(TokenRequest(Provider.GOOGLE, code, codeVerifier))
    }.body()

suspend fun requestMessagePage(code: Token): MessagePage =
    client.post(BuildConfig.BACKEND_URL + "/api/gmail/messages") {
        contentType(ContentType.Application.Json)
        setBody(MessageListRequest(code.access_token))
    }.body()

@OptIn(ExperimentalEncodingApi::class)
internal fun generateCodeVerifier(): String {
    val bytes = ByteArray(32) // 32 bytes = 256 bits (recommended size)
    SecureRandom().nextBytesCopyTo(bytes)
    return Base64.UrlSafe.encode(bytes)
        .replace("=", "") // Remove padding
        .replace("+", "-")
        .replace("/", "_")
}

@OptIn(ExperimentalEncodingApi::class)
internal fun generateCodeChallenge(codeVerifier: String): String {
    val byteString = codeVerifier.encodeToByteArray().toByteString()
    val hash = byteString.sha256()
    return Base64.UrlSafe.encode(hash.toByteArray()).replace("=", "")
}