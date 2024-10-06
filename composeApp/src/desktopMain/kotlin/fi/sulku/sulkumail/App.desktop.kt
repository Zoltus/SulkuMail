package fi.sulku.sulkumail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TopBar(loggedIn: Boolean, drawerState: DrawerState) {
    Row {
        //MenuButton(loggedIn, drawerState)

        Spacer(Modifier.weight(1f))

        TextButton(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = {/*::exitApp*/ }
        ) {
            Icon(Icons.Rounded.Close, contentDescription = null)
        }
    }
}