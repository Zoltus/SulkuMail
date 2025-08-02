package fi.sulku.sulkumail.composables.screens.mail_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.AiSidePanel
import kotlinx.coroutines.launch

/*
  onBack: () -> Unit,
    onSend: () -> Unit,
    onSaveDraft: () -> Unit,
    onAddAttachment: () -> Unit,
    onRemoveAttachment: (String) -> Unit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailEditorScreen() {
    val aiDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AiSidePanel(aiDrawerState = aiDrawerState) {
        Column(modifier = Modifier.fillMaxSize()) {
            val state = rememberRichTextState()

            RichTextHeaderToolBar(
                state = state,
                modifier = Modifier.fillMaxWidth(),
                isAiOpen = aiDrawerState.isOpen,
                onToggleAiPanel = {
                    scope.launch {
                        aiDrawerState.apply { if (isClosed) open() else close() }
                    }
                },
            )
            RichTextEditor(
                modifier = Modifier.fillMaxSize(),
                state = state,
            )
        }
    }
}