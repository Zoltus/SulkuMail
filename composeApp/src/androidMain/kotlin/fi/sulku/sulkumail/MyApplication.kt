package fi.sulku.sulkumail

import android.app.Application
import android.content.Context
import fi.sulku.sulkumail.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
    }

    companion object {
        private var instance: MyApplication? = null

        val applicationContext: Context
            get() = instance!!.applicationContext
    }
}
