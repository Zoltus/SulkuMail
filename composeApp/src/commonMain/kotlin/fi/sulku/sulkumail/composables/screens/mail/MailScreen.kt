package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fi.sulku.sulkumail.composables.content.SearchBarSample

@Composable
fun MailScreen(drawerState: DrawerState, email: String) {
    Column {
        SearchBarSample(drawerState)
        Text("MailScreen")
    }
}