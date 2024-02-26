package com.obidia.testagrii.data.local.room

import androidx.room.*
import com.obidia.testagrii.data.local.entity.NoteEntity
import com.obidia.testagrii.data.local.entity.NoteEntityAndSubEntity
import com.obidia.testagrii.data.local.entity.SubNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface
NoteDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addNote(data: NoteEntity)

  @Update
  fun updateNote(data: NoteEntity)

  @Delete
  fun deleteNote(data: NoteEntity)

  @Query("DELETE FROM note_table")
  fun deleteAllNotes()

  @Transaction
  @Query("SELECT * FROM note_table ORDER BY id ASC")
  fun getAllNotes(): Flow<List<NoteEntityAndSubEntity>>

  @Query("SELECT * FROM note_table where category = :key")
  fun getNoteByCategory(key: String): Flow<List<NoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addSubNote(data: SubNoteEntity)

  @Update
  fun updateSubNote(data: SubNoteEntity)

  @Delete
  fun deleteSubNote(data: SubNoteEntity)

  @Query("SELECT * FROM sub_note_table where idNote = :idNote ORDER BY idSubNote ASC")
  fun getAllSubNotes(idNote: Int): Flow<List<SubNoteEntity>>

  @Query("SELECT id FROM note_table ORDER BY id DESC LIMIT 1")
  fun getLatestNote(): Int

  @Query("DELETE FROM note_table WHERE id = :idNote")
  fun deleteNoteById(idNote: Int)

  @Query("UPDATE sub_note_table SET idNote = :idNote WHERE idNote = 0")
  fun updateSomeSubsNotes(idNote: Int)
}