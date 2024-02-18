package com.obidia.testagrii.data.repository

import com.obidia.testagrii.data.local.entity.NoteEntity
import com.obidia.testagrii.data.local.room.NoteDao
import com.obidia.testagrii.domain.model.NoteModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Singleton
class LocalDataSource @Inject constructor(
  private val noteDao: NoteDao
) {
  fun addNote(data: NoteModel) {
    noteDao.addNote(NoteEntity.transform(data))
  }

  fun updateNote(data: NoteModel) {
    noteDao.updateNote(NoteEntity.transform(data))
  }

  fun deleteNote(data: NoteModel) {
    noteDao.deleteNote(NoteEntity.transform(data))
  }

  fun deleteAllNotes() {
    noteDao.deleteAllNotes()
  }

  fun getAllNotes(): Flow<ArrayList<NoteModel>> = noteDao.getAllNotes().map {
    NoteEntity.transform(it)
  }.flowOn(Dispatchers.IO)

  fun getNoteByCategory(key: String): Flow<ArrayList<NoteModel>> = noteDao.getAllNotes().map {
    NoteEntity.transform(it)
  }.flowOn(Dispatchers.IO)
}