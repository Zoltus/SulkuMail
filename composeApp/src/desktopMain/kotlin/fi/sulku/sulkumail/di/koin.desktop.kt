package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.auth.UserRepository
import fi.sulku.sulkumail.auth.models.room.user.UserDatabase
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single {
        val dbFile = File(System.getProperty("user.home"), "mail.db")
        val dao = Room.databaseBuilder<UserDatabase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build().userDao()
        UserRepository(dao)
    }
}