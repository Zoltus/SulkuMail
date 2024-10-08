package fi.sulku.sulkumail.composables.sidebar

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    println("aa")
     if (Desktop.isDesktopSupported()) {
         println("Desktop supported")
        Desktop.getDesktop().browse(URI(url))
    }
}