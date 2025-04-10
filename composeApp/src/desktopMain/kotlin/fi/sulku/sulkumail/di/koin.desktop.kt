package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.data.repositories.UserRepository
import fi.sulku.sulkumail.data.auth.models.room.UserDatabase
import fi.sulku.sulkumail.data.repositories.MailRepository
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single {
        val dbFile = File(System.getProperty("user.home"), "mail.db")
        Room.databaseBuilder<UserDatabase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
            .userDao()
    }

    single { UserRepository(get()) }
    single { MailRepository(get()) }
}