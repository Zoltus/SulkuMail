package fi.sulku.sulkumail.di

import fi.sulku.sulkumail.auth.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::UserRepository)

   // val preferences = Preferences.userRoot() todo
   // val settings: Settings = PreferencesSettings(preferences)
    //single { SettingsRepository(settings) }

}