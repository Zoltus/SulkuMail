package fi.sulku.sulkumail.composables.screens.manageaccounts

import SulkuMail.shared.BuildConfig
import android.content.ActivityNotFoundException
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
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
actual fun PlatformGoogleLogin(
    scopes: List<String>,
    authResult: (AuthResult) -> Unit
) {
    val context: Context = LocalContext.current
    val authClient: AuthorizationClient = Identity.getAuthorizationClient(context)
    var authCompletable: CompletableDeferred<AuthorizationResult> = CompletableDeferred<AuthorizationResult>()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        try {
            val resultFromIntent = authClient.getAuthorizationResultFromIntent(result.data)
            authCompletable.complete(resultFromIntent)
        } catch (_: ApiException) {
            authResult(AuthResult.Error("Auth Interrupted!"))
        }
    }

    LaunchedEffect(Unit) {
        try {
            launchAuthFlow(context, scopes, authClient, authCompletable, launcher)
        } catch (e: AuthException) {
            authResult(AuthResult.Error(e.message))
        }
    }
}

suspend fun launchAuthFlow(
    context: Context,
    scopes: List<String>,
    authClient: AuthorizationClient,
    authCompletable: CompletableDeferred<AuthorizationResult>,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
): User {
    requestGoogleCredentials(context)
    // Ask for additional permissions
    val authRequest = AuthorizationRequest.Builder()
        .requestOfflineAccess(BuildConfig.GOOGLE_CLIENT_ID, true)
        .setRequestedScopes(scopes.map(::Scope))
        .build()

    var authResult: AuthorizationResult = authClient.authorize(authRequest).await() // todo check if throws
    val resolvedResult = resolveAuthResult(authResult, authCompletable, launcher)
    val serverAuthCode = resolvedResult?.serverAuthCode

    if (resolvedResult == null) {
        throw AuthException("Error resolving resolution!")
    } else if (serverAuthCode == null) {
        throw AuthException("ServerAuthCode is null!")
    } else {
        val token = exchangeCodeForToken(serverAuthCode)
        val userInfo = fetchUserInfo(token.access_token)
        return User(userInfo, token, EmailProvider.GMAIL)
    }
}

suspend fun requestGoogleCredentials(context: Context) {
    // Google credential manager
    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .setFilterByAuthorizedAccounts(true)
        .setAutoSelectEnabled(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
    // Launch account selection
    val credentialManager = CredentialManager.create(context)
    // Clear old credentials, incase old was interrupted
    //  val clearRequest = ClearCredentialStateRequest(ClearCredentialStateRequest.TYPE_CLEAR_CREDENTIAL_STATE)
    //    credentialManager.clearCredentialState(clearRequest)
    try {
        credentialManager.getCredential(context, request)
    } catch (_: Exception) {
        throw AuthException("Auth Interrupted!")
    }
}

// Checks if authResult is valid, if its needs resolving it handles it on activity and waits for result.
suspend fun resolveAuthResult(
    authResult: AuthorizationResult?,
    authCompletable: CompletableDeferred<AuthorizationResult>,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
): AuthorizationResult? {
    if (authResult == null) {
        null
    } else if (authResult.hasResolution()) {
        val intentSender = authResult.pendingIntent?.intentSender ?: throw NullPointerException("No intentSender found")
        try {
            launcher.launch(IntentSenderRequest.Builder(intentSender).build())
            authCompletable.await()
        } catch (_: ActivityNotFoundException) {
            null
        }
    }
    return authResult
}

internal suspend fun exchangeCodeForToken(code: String): Token {
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