package fi.sulku.sulkumail.koin

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//expect val platofrumModule: Module // defined in platform specific code
//https://www.youtube.com/watch?v=RlctqGVYESk
val appModule = module {
   // single<DataRepository> { DataRepositoryImpl() }
   // viewModel { ItemDefViewModel(get()) }
    //viewModel { ObjectDefViewModel(get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}