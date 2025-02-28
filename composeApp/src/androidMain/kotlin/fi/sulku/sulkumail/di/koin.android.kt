package fi.sulku.sulkumail.di

import android.preference.PreferenceManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import fi.sulku.sulkumail.auth.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val context = androidContext()
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val settingsRepository: Settings = SharedPreferencesSettings(sharedPrefs)

        UserRepository(settingsRepository)
    }
}


