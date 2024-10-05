package fi.sulku.sulkumail.composables.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview

//todo to viewmodel&Dataclass
data class Mail(
    val title: String,
    val email: String,
)

val mails = listOf(
    Mail("Mail1", "a@outlook.com"),
    Mail("Mail2", "b@gmail.com")
)

@Preview
@Composable
fun SideDrawer(
    nav: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    PermanentNavigationDrawer(
        content = content,
        drawerContent = {
            AnimatedVisibility(
                visible = drawerState.isOpen,
                enter = expandHorizontally(animationSpec = tween(durationMillis = 200)),
                exit = shrinkHorizontally(animationSpec = tween(durationMillis = 200))
            ) {
                PermanentDrawerSheet(
                    modifier = Modifier.width(280.dp),
                ) {
                    //todo cleanup, combine to 1 list so no need to + index ect
                    //todo some lazylist? so if many mails it scrolls?
                    //todo custom draweritem composable
                    NavigationDrawerItem(
                        shape = MaterialTheme.shapes.small,
                        label = { Text(text = "+ New Mail") },
                        // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                        selected = selectedItemIndex == 0,
                        badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                        onClick = {
                            //nav.navigate(mail.email)
                            selectedItemIndex = 0
                        },
                    )
                    mails.forEachIndexed { index, mail ->
                        //todo dropdown drawer thingy?
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            //modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            label = { Text(text = mail.title) },
                            // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                            selected = index + 1 == selectedItemIndex,
                            badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                            onClick = {
                                nav.navigate(mail.email)
                                selectedItemIndex = index + 1
                            },
                        )
                    }

                    //Moves items to bottom
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = "Manage Accounts") },
                            // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                            selected = selectedItemIndex == 0,
                            badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                            onClick = {
                                selectedItemIndex = 0
                            },
                        )

                        NavigationDrawerItem(
                            shape = MaterialTheme.shapes.small,
                            label = { Text(text = "Settings") },
                            // icon = { Icon(imageVector = mail.icon, contentDescription = mail.title) },
                            selected = selectedItemIndex == 0,
                            badge = { /*if (!mail.isSaved) Text(text = "*")*/ },
                            onClick = {
                                selectedItemIndex = 0
                            },
                        )
                    }
                }
            }
        }
    )
}