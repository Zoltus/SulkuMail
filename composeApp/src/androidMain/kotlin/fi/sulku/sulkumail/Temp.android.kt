package fi.sulku.sulkumail

actual suspend fun openUrl(url: String, onTokenReceived: suspend (String) -> Unit) {

    /*val context = applicationContext
    //Todo return back to app?
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    context.startActivity(browserIntent)*/
}