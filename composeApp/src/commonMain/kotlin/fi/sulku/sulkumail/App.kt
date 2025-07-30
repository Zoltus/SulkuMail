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
import fi.sulku.sulkumail.composables.screens.mail.view.MailView
import fi.sulku.sulkumail.composables.screens.mail_editor.MailEditorScreen
import fi.sulku.sulkumail.composables.screens.manageaccounts.ManageAccounts
import fi.sulku.sulkumail.composables.screens.settings.Settings
import fi.sulku.sulkumail.composables.sidebar.SideDrawer
import fi.sulku.sulkumail.routes.*
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
                .background(CustomColor.discordDark2)
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
                    composable<MailsRoute> { entry ->
                        MailScreen(nav = nav, drawerState = drawerState)
                    }
                     composable<MailEditorRoute> { entry ->
                        MailEditorScreen()
                    }
                    composable<MailRoute> { entry ->
                        val mailRoute: MailRoute = entry.toRoute()
                        MailView(mailRoute.mailId)
                    }
                }
            })
        }
    }
}



