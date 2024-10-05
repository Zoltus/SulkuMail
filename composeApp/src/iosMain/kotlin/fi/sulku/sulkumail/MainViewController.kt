package fi.sulku.sulkumail

import androidx.compose.ui.window.ComposeUIViewController
import fi.sulku.sulkumail.koin.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}