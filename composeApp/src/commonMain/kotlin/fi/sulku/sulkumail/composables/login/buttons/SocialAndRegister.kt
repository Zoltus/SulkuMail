package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.login.OrSeparator
import sulkumail.composeapp.generated.resources.Res
import sulkumail.composeapp.generated.resources.discord
import sulkumail.composeapp.generated.resources.google

@Composable
fun SocialArea() {
    val rainbowBrush = Brush.linearGradient(
        colors = listOf(
            Color.Red,
            Color.Yellow,
            Color.Green,
            Color.Blue,
        )
    )

    OrSeparator()
    Row {
        SocialButton("Google", Res.drawable.google, 16.dp, rainbowBrush, Color.Unspecified) {}
        Spacer(modifier = Modifier.width(16.dp))
        SocialButton("Discord", Res.drawable.discord, 32.dp, Color(0xFF5865F2), Color(0xFF5865F2)) {}
    }
}


/* https://tailwindflex.com/@anonymous/continue-with-discord-button-2
https://tailwindflex.com/@s-patel67/continue-with-google-button
https://docs.ultimatemember.com/article/1763-social-login-discord-app-setup
@Composable
fun ConnectWithDiscordButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5865F2)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(48.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_discord),
                contentDescription = "Discord",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Connect with Discord",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}*/