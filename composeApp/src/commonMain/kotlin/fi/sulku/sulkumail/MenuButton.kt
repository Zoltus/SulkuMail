package fi.sulku.sulkumail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MenuButton(loggedIn: Boolean, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    // Dont show menu button if user is not logged in
    if (!loggedIn) {
        return
    }

    TextButton(
        shape = MaterialTheme.shapes.extraSmall,
        onClick = {
            //todo cleanup?
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }
    ) {
        Icon(Icons.Rounded.Menu, contentDescription = null)
        Spacer(modifier = Modifier.Companion.width(8.dp))
        Text("Menu")
    }
}