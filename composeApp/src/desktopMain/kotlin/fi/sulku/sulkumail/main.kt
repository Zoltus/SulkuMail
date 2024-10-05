package fi.sulku.sulkumail

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fi.sulku.sulkumail.koin.appModule
import fi.sulku.sulkumail.koin.initKoin
import org.koin.core.context.startKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "SulkuMail",
    ) {
        App()
    }
}


@Composable
@Preview
fun test() {
    Text("asd")

}