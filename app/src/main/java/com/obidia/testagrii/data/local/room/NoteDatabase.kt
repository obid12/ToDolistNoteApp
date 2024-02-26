package com.obidia.testagrii.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.obidia.testagrii.data.local.entity.NoteEntity
import com.obidia.testagrii.data.local.entity.SubNoteEntity

@Database(entities = [NoteEntity::class, SubNoteEntity::class], version = 12, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
  abstract fun noteDao(): NoteDao
}