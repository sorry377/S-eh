package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.GeneratedMedia
import com.example.data.model.SavedFile
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun clearSessionMessages(sessionId: String)

    // Saved Files
    @Query("SELECT * FROM saved_files ORDER BY createdAt DESC")
    fun getAllSavedFiles(): Flow<List<SavedFile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedFile(file: SavedFile): Long

    @Query("DELETE FROM saved_files WHERE id = :id")
    suspend fun deleteSavedFile(id: Long)

    // Generated Media
    @Query("SELECT * FROM generated_media ORDER BY createdAt DESC")
    fun getAllGeneratedMedia(): Flow<List<GeneratedMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedMedia(media: GeneratedMedia): Long

    @Query("DELETE FROM generated_media WHERE id = :id")
    suspend fun deleteGeneratedMedia(id: Long)
}

@Database(
    entities = [ChatMessage::class, SavedFile::class, GeneratedMedia::class],
    version = 1,
    exportSchema = false
)
abstract class NovaDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
