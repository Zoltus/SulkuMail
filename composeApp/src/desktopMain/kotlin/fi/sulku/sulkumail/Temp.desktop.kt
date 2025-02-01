package fi.sulku.sulkumail

import io.ktor.server.engine.*
import io.ktor.server.netty.Netty
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.awt.Desktop
import java.net.URI

actual suspend fun openUrl(url: String, onTokenReceived: suspend (String) -> Unit) {
    startLocalServer { onTokenReceived(it) }
    Desktop.getDesktop().browse(URI(url))
}

//todo serializable & model // todo reqeust token here?
fun startLocalServer(onCodeReceived: suspend (String) -> Unit) {
    embeddedServer(Netty, port = 8079) {
        routing {
            get("/callback") {
                val code = call.request.queryParameters["code"]
                if (code != null) {
                    onCodeReceived(code)
                    call.respondText("Authentication successful! You can close this window.")
                } else {
                    call.respondText("Error: No code received.")
                }
            }
        }
    }.start(wait = false)
}
