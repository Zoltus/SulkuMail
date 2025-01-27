package fi.sulku.sulkumail

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import fi.sulku.sulkumail.di.initKoin
import fi.sulku.sulkumail.viewmodels.AuthViewModel
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.compose.viewmodel.koinViewModel

//todo temp
lateinit var appContext: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKoin()
        setContent {
            val authVm = koinViewModel<AuthViewModel>()
            authVm.supabase.handleDeeplinks(intent)
            App()
            appContext = LocalContext.current
        }
    }
}