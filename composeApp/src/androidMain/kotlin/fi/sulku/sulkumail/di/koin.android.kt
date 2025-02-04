package fi.sulku.sulkumail.di

import android.preference.PreferenceManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import fi.sulku.sulkumail.MyApplication.Companion.applicationContext
import org.koin.dsl.module

actual val platformModule = module {
    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext) // todo dep
    val settingsRepository: Settings = SharedPreferencesSettings(sharedPrefs)
    single { SettingsRepository(settingsRepository) }
}


