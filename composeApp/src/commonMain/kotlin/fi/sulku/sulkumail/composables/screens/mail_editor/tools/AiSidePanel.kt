package fi.sulku.sulkumail.composables.screens.mail_editor.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai.PromptField
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai.AiViewModel
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai.PromptButton
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai.ResponsesArea
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AiSidePanel(
    aiDrawerState: DrawerState,
    mailContent: @Composable () -> Unit
) {
    val aiVm = koinViewModel<AiViewModel>()
    val messages by aiVm.responses.collectAsState()

    val inputText = remember { mutableStateOf("") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = aiDrawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        modifier = Modifier.Companion.width(320.dp),
                        drawerContainerColor = Color(0xFF1E1E1E),
                        drawerShape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.Companion
                                .align(Alignment.Companion.End)
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ResponsesArea(messages = messages)

                            PromptField(inputText)

                            Row(
                                modifier = Modifier.Companion.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PromptButton(aiVm, inputText)
                            }
                        }
                    }
                }
            },
            //gesturesEnabled = isOpen,
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    mailContent()
                }
            }
        )
    }
}