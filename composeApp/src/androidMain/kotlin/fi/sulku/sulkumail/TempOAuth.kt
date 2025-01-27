package fi.sulku.sulkumail

import android.content.Intent
import android.net.Uri

actual fun getRedirectUrl() = "yourapp://oauth"

actual suspend fun handleOAuthCallback(url: String) {
   // supabase.auth.onAuthCallback(url)
}

actual fun openBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    appContext.startActivity(intent)
}