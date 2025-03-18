package fi.sulku.sulkumail.auth.models.room.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [User::class, MailEntity::class],
    version = 9,
)
@TypeConverters(DatabaseConverters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}