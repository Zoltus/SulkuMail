package fi.sulku.sulkumail.di

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import fi.sulku.sulkumail.auth.UserRepository
import org.koin.dsl.module
import java.util.prefs.Preferences

actual val platformModule = module {
    val preferences = Preferences.userRoot()
    val settings: Settings = PreferencesSettings(preferences)
    single { UserRepository(settings) }
}