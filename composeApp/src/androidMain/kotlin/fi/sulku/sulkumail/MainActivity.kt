package fi.sulku.sulkumail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fi.sulku.sulkumail.di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin()
        setContent {
            App()
        }
    }
}