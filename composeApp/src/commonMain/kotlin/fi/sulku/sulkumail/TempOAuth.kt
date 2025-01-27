package fi.sulku.sulkumail

import androidx.compose.runtime.Composable

expect fun getRedirectUrl(): String
expect suspend fun handleOAuthCallback(url: String)

expect fun openBrowser(url: String)