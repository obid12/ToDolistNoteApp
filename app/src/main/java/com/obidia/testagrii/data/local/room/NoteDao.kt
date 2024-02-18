package com.obidia.testagrii.data.local.room

import androidx.room.*
import com.obidia.testagrii.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(data: NoteEntity)

    @Update
    fun updateNote(data: NoteEntity)

    @Delete
    fun deleteNote(data: NoteEntity)

    @Query("DELETE FROM user_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM user_table where category = :key")
    fun getNoteByCategory(key: String): Flow<List<NoteEntity>>
}