package fi.sulku.sulkumail.composables.screens.manageaccounts

import SulkuMail.shared.BuildConfig
import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import fi.sulku.sulkumail.AndroidTokenRequest
import fi.sulku.sulkumail.AuthResponse
import fi.sulku.sulkumail.Provider
import fi.sulku.sulkumail.auth.UserViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun PlatformGoogleLogin(scopes: List<String>, onAuthResponse: (AuthResponse) -> Unit) {
    val authVm: UserViewModel = koinViewModel<UserViewModel>()
    val context = LocalContext.current
    val authorizationLauncher = rememberAuthorizationLauncher(context = context, authVm, onAuthResponse)
    val googleAuthProvider = GoogleAuthProvider.create(GoogleAuthCredentials(serverId = BuildConfig.GOOGLE_CLIENT_ID))
    val googleAuthUiProvider = googleAuthProvider.getUiProvider()

    LaunchedEffect(Unit) {
        val googleUser = googleAuthUiProvider.signIn(
            filterByAuthorizedAccounts = true,
            scopes = scopes
        )
        if (googleUser != null) {
            val authRequest = AuthorizationRequest.Builder()
                .requestOfflineAccess(BuildConfig.GOOGLE_CLIENT_ID, true)
                .setRequestedScopes(scopes.map(::Scope))
                .build()

            val authClientResult = Identity.getAuthorizationClient(context)
                .authorize(authRequest).await()

            if (authClientResult.hasResolution()) {
                val intentSender = authClientResult.pendingIntent?.intentSender
                if (intentSender != null) {
                    authClientResult.pendingIntent?.intentSender?.let { intentSender ->
                        authorizationLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                    }
                }
            } else if (authClientResult.serverAuthCode != null) {
                exchangeCodeForToken(authClientResult.serverAuthCode!!, onAuthResponse)
            }
        }
    }
}


@Composable
private fun rememberAuthorizationLauncher(
    context: Context,
    authVm: UserViewModel = koinViewModel<UserViewModel>(),
    onAuthResponse: (AuthResponse) -> Unit
): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val authClientResult = Identity.getAuthorizationClient(context)
                .getAuthorizationResultFromIntent(activityResult.data)
            authVm.viewModelScope.launch {
                exchangeCodeForToken(authClientResult.serverAuthCode!!, onAuthResponse) // todo !!
            }
        }
    }
}

private suspend fun exchangeCodeForToken(code: String, onAuthResponse: (AuthResponse) -> Unit): AuthResponse {
    return HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }.use { client ->
        val response = client.post(BuildConfig.BACKEND_URL + "/api/auth/android") {
            contentType(ContentType.Application.Json)
            setBody(
                AndroidTokenRequest(
                    Provider.GOOGLE,
                    code,
                )
            )
        }.body<AuthResponse>()
        onAuthResponse(response)
        response
    }
}