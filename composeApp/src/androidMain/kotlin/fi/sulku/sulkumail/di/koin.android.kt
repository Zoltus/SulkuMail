package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.auth.UserRepository
import fi.sulku.sulkumail.auth.models.room.user.UserDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val context = androidContext()
        val dbFile = context.getDatabasePath("mail.db")
        val dao = Room.databaseBuilder<UserDatabase>(context.applicationContext, name = dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .build()
            .userDao()
        UserRepository(dao)
    }
}


