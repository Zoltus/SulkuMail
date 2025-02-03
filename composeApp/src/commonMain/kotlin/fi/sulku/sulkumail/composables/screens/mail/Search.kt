package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import fi.sulku.sulkumail.MessagesResp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.Search(
    drawerState: DrawerState,
    messageResp: MessagesResp
) {
    val scope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Hinted search text") },
                leadingIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)

                },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        LazyColumn {
            itemsIndexed(messageResp.messages) { index, mail ->
                ListItem(
                    headlineContent = { Text("Res $index") },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .clickable {
                            query = "Res $index"
                            expanded = false
                        }
                )
            }
        }
    }
}