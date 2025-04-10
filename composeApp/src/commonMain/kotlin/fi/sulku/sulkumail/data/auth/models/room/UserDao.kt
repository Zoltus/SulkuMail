package fi.sulku.sulkumail.data.auth.models.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(mail: MailEntity)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun trashMail(mail: MailEntity)

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM emails WHERE userId = :userId AND labelIds LIKE '%INBOX%'")
    fun getInbox(userId: Int): Flow<List<MailEntity>>

    @Query("SELECT * FROM emails WHERE userId = :userId AND labelIds LIKE '%TRASH%'")
    fun getTrash(userId: Int): Flow<List<MailEntity>>

    @Query("SELECT id FROM emails WHERE userId = :id AND id IN (:ids)")
    suspend fun dupeIds(id: Int, ids: List<String>): List<String>
}