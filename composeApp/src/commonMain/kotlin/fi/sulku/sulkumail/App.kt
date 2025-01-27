package fi.sulku.sulkumail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fi.sulku.sulkumail.composables.screens.mail.MailScreen
import fi.sulku.sulkumail.composables.screens.manageaccounts.ManageAccounts
import fi.sulku.sulkumail.composables.screens.settings.Settings
import fi.sulku.sulkumail.composables.sidebar.SideDrawer
import fi.sulku.sulkumail.theme.AppTheme
import fi.sulku.sulkumail.theme.CustomColor
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() = AppTheme {
    KoinContext {
        val authVm = koinViewModel<AuthViewModel>()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val nav: NavHostController = rememberNavController()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomColor.discordDark)
        ) {
            SideDrawer(nav, drawerState) {
                NavHost(
                    navController = nav,
                    startDestination = ManageAccountsRoute,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                ) {
                    composable<ManageAccountsRoute> {
                        ManageAccounts()
                    }
                    composable<SettingsRoute> {
                        Settings()
                    }
                    composable<MailRoute> { entry ->
                        val mail = entry.toRoute<MailRoute>()
                        MailScreen(
                            drawerState = drawerState,
                            email = mail.email,
                        )
                    }
                }
            }
        }
    }
}

//todo move
@Serializable
data class MailRoute(val email: String)

@Serializable
object ManageAccountsRoute

@Serializable
object SettingsRoute



