package fi.sulku.sulkumail

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.datlag.kcef.KCEF
import dev.datlag.kcef.KCEFBuilder
import fi.sulku.sulkumail.di.initKoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext
import java.io.File
import kotlin.math.max

fun main() = application {
    initKoinOnce()
    var isVisible by remember { mutableStateOf(true) }
    Window(
        title = "SulkuMail",
        visible = isVisible,
        onCloseRequest = { isVisible = false },

        state = rememberWindowState(
            width = 1200.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        ),
    ) {

        var restartRequired by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(0F) }
        var initialized by remember { mutableStateOf(false) }
        val download: KCEFBuilder.Download = remember { KCEFBuilder.Download.Builder().github().build() }


        if (restartRequired) {
            Text(text = "Restart required.")
        } else {
            if (initialized) {
                App()
                if (!isVisible) {
                    Tray(
                        icon = DefaultTrayIcon,
                        tooltip = "My App",
                        onAction = { isVisible = true },
                        menu = {
                            Item("Open", onClick = { isVisible = true })
                            Item("Exit", onClick = ::exitApplication)
                        }
                    )
                }
            } else {
                Text(text = "Downloading $downloading%")
            }
        }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                KCEF.init(builder = {
                    installDir(File("kcef-bundle"))
                    progress {
                        onDownloading {
                            downloading = max(it, 0F)
                        }
                        onInitialized {
                            initialized = true
                        }
                    }
                    settings {
                        cachePath = File("cache").absolutePath
                    }
                }, onError = {
                    it!!.printStackTrace()
                }, onRestartRequired = {
                    restartRequired = true
                })
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                KCEF.disposeBlocking()
            }
        }
    }
}

private fun initKoinOnce() {
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }
}

object DefaultTrayIcon : Painter() {
    override val intrinsicSize: Size
        get() = Size(24f, 24f)

    override fun DrawScope.onDraw() {
        drawOval(color = Color(0xFF1976D2))
    }
}