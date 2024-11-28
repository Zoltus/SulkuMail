package fi.sulku.sulkumail.di

import fi.sulku.sulkumail.viewmodels.AuthViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(viewModelModule, supabaseModule)
    }
}
