package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import fi.sulku.sulkumail.composables.login.OrSeparator
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.ui.ProviderButtonContent
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental

@OptIn(AuthUiExperimental::class)
@Composable
fun SocialArea() {
/*    val rainbowBrushGoogle = Brush.linearGradient(
        colors = listOf(
            Color.Red,
            Color.Yellow,
            Color.Green,
            Color.Blue,
        )
    )*/
    OrSeparator()
    OutlinedButton(
        onClick = {

        },
        content = { ProviderButtonContent(Google) }
    )
    OutlinedButton(
        onClick = {

        },
        content = { ProviderButtonContent(Discord) }
    )
}