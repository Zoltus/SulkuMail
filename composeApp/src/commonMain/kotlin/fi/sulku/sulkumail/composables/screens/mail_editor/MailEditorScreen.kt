package fi.sulku.sulkumail.composables.screens.mail_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

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
    Column(modifier = Modifier.fillMaxSize()) {
        val state = rememberRichTextState()

        RichTextHeaderToolBar(state = state, modifier = Modifier.fillMaxWidth())
        RichTextEditor(
            modifier = Modifier.fillMaxSize(),
            state = state
        )
        /* RichTextBottomToolBar(
             modifier = Modifier.fillMaxWidth(),
             state = state,
             onClose = { }
         )*/
    }

}