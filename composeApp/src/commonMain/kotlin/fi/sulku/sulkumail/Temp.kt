package fi.sulku.sulkumail

expect suspend fun openUrl(url: String, onTokenReceived: suspend (String) -> Unit)