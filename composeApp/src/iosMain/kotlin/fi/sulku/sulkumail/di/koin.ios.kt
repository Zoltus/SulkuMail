package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.data.repositories.UserRepository
import fi.sulku.sulkumail.data.auth.models.room.UserDatabase
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformModule = module {
    single {
        val dbFile = NSHomeDirectory() + "/mail.db"
        Room.databaseBuilder<UserDatabase>(name = dbFile, factory = { UserDatabase::class.instantiateImpl() })
            .setDriver(BundledSQLiteDriver())
            .build()
            .userDao()
    }

    single { UserRepository(get()) }
    single { MailRepository(get()) }
}