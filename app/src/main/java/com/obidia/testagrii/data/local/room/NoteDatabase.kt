package com.obidia.testagrii.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.obidia.testagrii.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 6, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
  abstract fun noteDao(): NoteDao
}