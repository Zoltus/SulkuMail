package fi.sulku.sulkumail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fi.sulku.sulkumail.composables.sidebar.SideDrawer
import fi.sulku.sulkumail.composables.sidebar.mails
import fi.sulku.sulkumail.theme.AppTheme
import fi.sulku.sulkumail.theme.CustomColor
import org.koin.compose.KoinContext

@Composable
fun App() = AppTheme {
    KoinContext {
        //val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val nav: NavHostController = rememberNavController()

        val loggedIn by remember { mutableStateOf(true) } //todo to vm


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomColor.discordDark)
        ) {
            // todo topbar remove from expect,
            // todo add only from desktopmain & wasm access drawerstate from vm
            //TopBar(loggedIn, drawerState)

            SideDrawer(nav, drawerState) {
                NavHost(
                    navController = nav,
                    startDestination = "welcome",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                ) {
                    //Route for welcome screen
                    composable(route = "welcome") {
                        WelcomeScreen()
                    }
                    //Routes for emails
                    mails.forEach { mail ->
                        composable(route = mail.email) {
                            //MailScreen(mail.email)
                            SearchBarSample(drawerState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
expect fun TopBar(loggedIn: Boolean, drawerState: DrawerState)


@Composable
fun MailScreen(email: String) {
    Text("Email of theasd mail: $email")
}

@Composable
fun WelcomeScreen() {
    Text("Welcome to Sulkumail")
}