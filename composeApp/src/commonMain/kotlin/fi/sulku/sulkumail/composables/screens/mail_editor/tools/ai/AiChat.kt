package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun AiSidePanel(
    aiDrawerState: DrawerState,
    onConfirm: (String) -> Unit,
    content: @Composable () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = aiDrawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        modifier = Modifier.width(320.dp),
                        drawerContainerColor = Color(0xFF1E1E1E),
                        drawerShape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Ask AI to type",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White
                            )

                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                label = { Text("Describe what AI should write") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp),
                                singleLine = false,
                                maxLines = 8,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF2B2B2B),
                                    unfocusedContainerColor = Color(0xFF2B2B2B),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = MaterialTheme.colorScheme.primary,
                                    focusedLabelColor = Color.Gray,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onConfirm(inputText)
                                        inputText = ""
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Generate")
                                }
                            }
                        }
                    }
                }
            },
            //gesturesEnabled = isOpen,
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        )
    }
}