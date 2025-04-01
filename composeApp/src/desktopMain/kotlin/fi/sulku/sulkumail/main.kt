package fi.sulku.sulkumail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import fi.sulku.sulkumail.di.initKoin
import org.koin.core.context.GlobalContext

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