package fi.sulku.sulkumail

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import fi.sulku.sulkumail.di.initKoin

fun main() = application {
    initKoin()
    Window(
        title = "SulkuMail",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        ),
    ) {
         App()
    }
}