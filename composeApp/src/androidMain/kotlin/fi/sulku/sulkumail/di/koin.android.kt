package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.data.auth.models.room.UserDatabase
import fi.sulku.sulkumail.data.repositories.MailRepository
import fi.sulku.sulkumail.data.repositories.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val context = androidContext()
        val dbFile = context.getDatabasePath("mail.db")
        Room.databaseBuilder<UserDatabase>(context.applicationContext, name = dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .build()
            .userDao()
    }

    single { UserRepository(get()) }
    single { MailRepository(get()) }
}

