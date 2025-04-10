package fi.sulku.sulkumail.data.auth

import SulkuMail.shared.BuildConfig
import android.content.ActivityNotFoundException
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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

internal actual suspend fun startGoogleAuthFlow(scopes: List<String>): Token {
    val activityContext = ActivityHolder.getActivity()
    if (activityContext != null) {
        val authClient: AuthorizationClient = Identity.getAuthorizationClient(activityContext)
        val authCompletable = CompletableDeferred<AuthorizationResult>()

        val launcher = ActivityHolder.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            try {
                val resultFromIntent = authClient.getAuthorizationResultFromIntent(result.data)
                authCompletable.complete(resultFromIntent)
            } catch (e: ApiException) {
                authCompletable.completeExceptionally(AuthException("Auth Interrupted3" + e.message))
            }
        }
        // Launch Google Credentials flow
        requestGoogleCredentials(activityContext)

        // Ask for additional permissions
        val authRequest = AuthorizationRequest.Builder()
            .requestOfflineAccess(BuildConfig.GOOGLE_CLIENT_ID, true)
            .setRequestedScopes(scopes.map(::Scope))
            .build()
        val authResult: AuthorizationResult? = authClient.authorize(authRequest).await()

        val resolvedResult = resolveAuthResult(authResult, authCompletable, launcher) ?: authResult
        val serverAuthCode = resolvedResult?.serverAuthCode

        if (resolvedResult == null) {
            throw AuthException("Error resolving resolution!")
        } else if (serverAuthCode == null) {
            throw AuthException("ServerAuthCode is null!")
        } else {
            return exchangeCodeForToken(serverAuthCode)
        }
    } else {
        throw AuthException("Auth Interrupted2")
    }
}


// Checks if authResult is valid, if its needs resolving it handles it on activity and waits for result.
private suspend fun resolveAuthResult(
    authResult: AuthorizationResult?,
    authCompletable: CompletableDeferred<AuthorizationResult>,
    launcher: ActivityResultLauncher<IntentSenderRequest>?
): AuthorizationResult? {
    if (launcher != null && authResult?.hasResolution() == true) {
        val intentSender = authResult.pendingIntent?.intentSender ?: throw NullPointerException("No intentSender found")
        try {
            val request = IntentSenderRequest.Builder(intentSender).build()
            launcher.launch(request)
            return authCompletable.await()
        } catch (e: ActivityNotFoundException) {
            return null
        }
    } else {
        return authResult
    }
}


private suspend fun requestGoogleCredentials(context: Context) {
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
    /*    // Clear old credentials, incase old was interrupted
          val clearRequest = ClearCredentialStateRequest(ClearCredentialStateRequest.TYPE_CLEAR_CREDENTIAL_STATE)
           credentialManager.clearCredentialState(clearRequest)*/
    try {
        credentialManager.getCredential(context, request)
    } catch (e: Exception) {
        throw AuthException("Auth Interrupted!1 " + e.message)
    }
}


internal suspend fun exchangeCodeForToken(code: String): Token { // todo move
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