package fi.sulku.sulkumail.composables.screens.mail.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import fi.sulku.sulkumail.composables.screens.mail.MailViewModel
import fi.sulku.sulkumail.data.auth.UserViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// val state = rememberRichTextState()
//  state.setHtml(mail!!.htmlBody ?: "No content available")
// RichText(state = state)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailView(mailId: String) {
    val userVm = koinInject<UserViewModel>()
    val user by userVm.selectedUser.collectAsState()

    val mailVm = koinViewModel<MailViewModel>()
    val mail by mailVm.getMail(user!!, mailId).collectAsState(initial = null)

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
        // .padding(paddingValues)
    ) {
        if (mail == null) {
            Text("Null Mail")
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Subject
            Text(
                text = mail!!.subject,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider()
            // Sender Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /* AsyncImage( // Use Coil or Glide for image loading
                     model = mail.senderImage, // Can be URL or other model Coil supports
                     contentDescription = "Sender Image",
                     modifier = Modifier
                         .size(40.dp)
                         .padding(end = 12.dp)
                     // Add placeholder/error for the image if needed
                 )*/
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mail!!.from,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "To: me", // Assuming 'me' for simplicity, could be parsed
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                // Date
                Text(
                    text = mail!!.date.toString(), // todo format date properly
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Email Body
            // If 'fullBody' is fetched separately and available, use it.
            // Otherwise, fallback to snippet or a loading message.
            // Simple Text rendering. For HTML, you'd need a WebView.

            val initialUrl = mail!!.htmlBody!! // todo improve
            val state = rememberWebViewStateWithHTMLData(initialUrl)
            // WebView loading indicator
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = { loadingState.progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // WebView
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                WebView(
                    state = state,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Display Labels (optional)
            mail!!.labelIds.let { labels ->
                if (labels.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Labels:", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        labels.forEach { label ->
                            SuggestionChip(onClick = { /* Optional action */ }, label = { Text(label) })
                        }
                    }
                }
            }
        }
    }
}



