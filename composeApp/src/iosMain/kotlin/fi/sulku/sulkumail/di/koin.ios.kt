package fi.sulku.sulkumail.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fi.sulku.sulkumail.auth.UserRepository
import fi.sulku.sulkumail.auth.models.room.user.UserDatabase
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformModule = module {
    single {
        val dbFile = NSHomeDirectory() + "/mail.db"
        val dao = Room.databaseBuilder<UserDatabase>(name = dbFile, factory = { UserDatabase::class.instantiateImpl() })
            .setDriver(BundledSQLiteDriver())
            .build()
            .userDao()
        UserRepository(dao)
    }
}