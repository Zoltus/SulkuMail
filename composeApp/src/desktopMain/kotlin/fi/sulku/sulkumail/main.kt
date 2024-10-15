package fi.sulku.sulkumail

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fi.sulku.sulkumail.koin.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "SulkuMail",
    ) {
        App()
    }
}