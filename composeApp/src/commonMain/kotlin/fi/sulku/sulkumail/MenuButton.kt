package fi.sulku.sulkumail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuButton(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

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
    }
}