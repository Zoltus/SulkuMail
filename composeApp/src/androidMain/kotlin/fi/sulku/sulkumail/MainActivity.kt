package fi.sulku.sulkumail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.di.initKoin
import io.github.jan.supabase.compose.auth.ui.annotations.AuthUiExperimental
import io.github.jan.supabase.compose.auth.ui.email.EmailField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supabase.handleDeeplinks(intent)
        initKoin()
        setContent {
            App()
        }
    }
}


@OptIn(AuthUiExperimental::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun test() {
    Column {
        // Login
        EmailField(
            modifier = Modifier
                .padding(0.dp)
                .heightIn(min = 0.dp),
            value = "email",
            onValueChange = { },
            label = { Text("E-Mail") },
            supportingText = null,
        )

        EmailField(
            modifier = Modifier
                .padding(0.dp)
                .heightIn(min = 0.dp),
            value = "email",
            onValueChange = { "email = it " },
            label = { Text("E-Mail") },
        )
    }

}