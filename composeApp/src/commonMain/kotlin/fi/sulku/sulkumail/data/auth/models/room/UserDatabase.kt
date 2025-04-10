package fi.sulku.sulkumail.data.auth.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [User::class, MailEntity::class],
    version = 1,
)
@TypeConverters(value = [UserConverters::class])
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}