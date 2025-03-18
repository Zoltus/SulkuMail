package fi.sulku.sulkumail.auth.models.room.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: MailEntity)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteEmail(email: MailEntity)

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM emails WHERE userId = :userId")
    fun getMails(userId: Int): Flow<List<MailEntity>>

    @Query("SELECT id FROM emails WHERE userId = :id AND id IN (:ids)")
    suspend fun dupeIds(id: Int, ids: List<String>): List<String>
}