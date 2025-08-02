package fi.sulku.sulkumail.di

import fi.sulku.sulkumail.composables.screens.mail.MailViewModel
import fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai.AiViewModel
import fi.sulku.sulkumail.composables.sidebar.DrawerViewModel
import fi.sulku.sulkumail.data.auth.AuthViewModel
import fi.sulku.sulkumail.data.auth.UserViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModel { AiViewModel() }
    viewModel { DrawerViewModel() }
    viewModel { AuthViewModel(userRepo = get()) }
    single { UserViewModel(userRepo = get()) } // Shared user across the app so singleton.
    viewModel { MailViewModel(mailRepo = get()) }
}

//https://github.com/getspherelabs/anypass-kmp/blob/902a0505c5eaf0f3848a5e06afaec98c1ed35584/data/prefs/src/commonMain/kotlin/io/spherelabs/data/settings/di/Koin.kt

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}
