package fi.sulku.sulkumail.composables.screens.manageaccounts

import SulkuMail.shared.BuildConfig
import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import fi.sulku.sulkumail.AndroidTokenRequest
import fi.sulku.sulkumail.Provider
import fi.sulku.sulkumail.Token
import fi.sulku.sulkumail.auth.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

@Composable
actual fun PlatformGoogleLogin(scopes: List<String>, onAuthResponse: (User) -> Unit) {
    val context = LocalContext.current
    val authClient = Identity.getAuthorizationClient(context)
    var authCompletable = remember { CompletableDeferred<AuthorizationResult?>() }

    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val resultFromIntent = authClient.getAuthorizationResultFromIntent(result.data)
            authCompletable.complete(resultFromIntent)
        }

    LaunchedEffect(Unit) {
        val authRequest = AuthorizationRequest.Builder()
            .requestOfflineAccess(BuildConfig.GOOGLE_CLIENT_ID, true)
            .setRequestedScopes(scopes.map(::Scope))
            .build()
        val authResult = authClient.authorize(authRequest).await()

        val account = if (authResult?.hasResolution() == true) {
            val intentSender = authResult.pendingIntent?.intentSender ?: throw NullPointerException("No intentSender found")
            try {
                activityResultLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                authCompletable.await()
            } catch (e: ActivityNotFoundException) {
                throw ActivityNotFoundException("No intent sender found!") // todo
            }
        } else {
            authResult
        }
        val serverAuthCode = account?.serverAuthCode
            ?: throw NoCredentialException("Authentication Failed: No token received")

        val token = exchangeCodeForToken(serverAuthCode)
        val userInfo = fetchUserInfo(token.access_token)
        val user = User(userInfo, token, EmailProvider.GMAIL)
        onAuthResponse(user)
    }
}

private suspend fun exchangeCodeForToken(code: String): Token {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }.use { client ->
        return client.post(BuildConfig.BACKEND_URL + "/api/auth/android") {
            contentType(ContentType.Application.Json)
            setBody(
                AndroidTokenRequest(
                    Provider.GOOGLE,
                    code,
                )
            )
        }.body<Token>()
    }
}