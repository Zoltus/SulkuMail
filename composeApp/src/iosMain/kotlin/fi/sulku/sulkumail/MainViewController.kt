package fi.sulku.sulkumail

import androidx.compose.ui.window.ComposeUIViewController
import fi.sulku.sulkumail.di.initKoin

//todo ios, mac ect support?
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}