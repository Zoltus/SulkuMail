package fi.sulku.sulkumail

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable

@Composable
actual fun TopBar(loggedIn: Boolean, drawerState: DrawerState) {
    MenuButton(loggedIn, drawerState)
}