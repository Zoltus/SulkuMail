package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.material3.*
import androidx.compose.runtime.*
import fi.sulku.sulkumail.UserData
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun DrawerBottom() {
    HorizontalDivider()
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Manage Accounts") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = {
        },
    )
    NavigationDrawerItem(
        shape = MaterialTheme.shapes.small,
        label = { Text(text = "Settings") },
        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
        selected = false,
        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
        onClick = {},
    )
    //todo temp
    DiscordLoginButton()
}

//todo return?
expect fun openUrl(url: String)

val json = Json { ignoreUnknownKeys = true }

val client = HttpClient() {
    install(ContentNegotiation) { json }
}

@Composable
fun DiscordLoginButton() {
    val scope = rememberCoroutineScope()
    var isLogged by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val client = HttpClient {
            install(WebSockets)
        }
        client.webSocket("ws://127.0.0.1:8080/ws/userid1") {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    //val userData = Json.decodeFromString<UserData>(frame.readText())
                    isLogged = true
                    println("Logged In")
                   // println("userData: $userData")
                    println("frame: ${frame.readText()}")
                }
            }
        }
    }




    TextButton(
        onClick = {

            scope.launch {
                //todo need to open in url
                try {
                    //http://127.0.0.1:8080/auth/discord/callback" http://127.0.0.1:8080/auth/discord
                    //Response discord auth link wiht client id and redirect uri, backend side
                    //val redirectResponse: HttpResponse = client.get("http://127.0.0.1:8080/auth/discord")
                    //openUrl()
                    //println(redirectUrl)
                    // val authUrl = redirectResponse.bodyAsText()
                    //println("redirectUrl: $authUrl")
                    openUrl("http://127.0.0.1:8080/auth/discord")
                } catch (e: Exception) {
                    println("Failed to connect: ${e.message}")
                }
            }
        }
    ) {
        Text("Login with Discord")
    }
}

/*
val dotenv = dotenv()
val clientId: String = dotenv["DISCORD_CLIENT_ID"]
val clientSecret: String = dotenv["DISCORD_CLIENT_SECRET"]
val redirectUri: String = dotenv["DISCORD_REDIRECT_URI"]
*/