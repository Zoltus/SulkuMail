package fi.sulku.sulkumail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.navigation.toRoute
import fi.sulku.sulkumail.composables.login.Login
import fi.sulku.sulkumail.composables.sidebar.SideDrawer
import fi.sulku.sulkumail.composables.sidebar.mails
import fi.sulku.sulkumail.theme.AppTheme
import fi.sulku.sulkumail.theme.CustomColor
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() = AppTheme {
    KoinContext {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val nav: NavHostController = rememberNavController()

        val authVm = koinViewModel<AuthViewModel>()
        val isLoggedIn: Boolean by authVm.isLoggedIn.collectAsState()
        LaunchedEffect(isLoggedIn) {
            println("Islogged: $isLoggedIn")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomColor.discordDark)
        ) {

            if (isLoggedIn) {
                Text("You are logged in")
                Button(onClick = {
                    authVm.signOut()
                }) {
                    Text("Logout")
                }
                return@KoinContext
            } else {
                Login()
                return@KoinContext
            }

            SideDrawer(nav, drawerState) {
                NavHost(
                    navController = nav,
                    startDestination = WelcomeRoute,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                ) {
                    composable<WelcomeRoute> {
                        //onNav = nav.navigate(WelcomeRoute)
                        Login()
                    }
                    // Login
                    composable<LoginRoute> {
                        Login()
                    }
                    // Routes for emails
                    mails.forEach { mail ->
                        composable<MailRoute> { backStackEntry ->
                            val mailRoute: MailRoute = backStackEntry.toRoute()
                            //MailRoute(mailRoute.email)
                            SearchBarSample(drawerState)
                        }
                        /*
                        composable<MailRoute> {
                            nav.navigate(MailRoute(mail.email))
                            SearchBarSample(drawerState)
                        }
                        composable(route = mail.email) {
                            //MailScreen(mail.email)
                            SearchBarSample(drawerState)
                        }
                         */
                    }
                }
            }
        }
    }
}

@Serializable
data class MailRoute(val email: String)

@Serializable
object WelcomeRoute

@Serializable
object LoginRoute




