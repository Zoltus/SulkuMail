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
import fi.sulku.sulkumail.composables.screens.mail.MailScreen
import fi.sulku.sulkumail.composables.screens.manageaccounts.ManageAccounts
import fi.sulku.sulkumail.composables.screens.settings.Settings
import fi.sulku.sulkumail.composables.sidebar.SideDrawer
import fi.sulku.sulkumail.routes.MailRoute
import fi.sulku.sulkumail.routes.ManageAccountsRoute
import fi.sulku.sulkumail.routes.SettingsRoute
import fi.sulku.sulkumail.theme.AppTheme
import fi.sulku.sulkumail.theme.CustomColor
import org.koin.compose.KoinContext

@Composable
fun App() = AppTheme {
    KoinContext {
        val nav: NavHostController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomColor.discordDark)
        ) {
            SideDrawer(nav, drawerState, content = {
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
                        MailScreen(drawerState = drawerState)
                    }
                }
            })
        }
    }
}



