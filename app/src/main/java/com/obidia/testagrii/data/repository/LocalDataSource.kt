package com.obidia.testagrii.data.repository

import com.obidia.testagrii.data.local.entity.NoteEntity
import com.obidia.testagrii.data.local.entity.NoteEntityAndSubEntity
import com.obidia.testagrii.data.local.entity.SubNoteEntity
import com.obidia.testagrii.data.local.room.NoteDao
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
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

  fun getAllNotes(): Flow<ArrayList<NoteAndSubNoteModel>> = noteDao.getAllNotes().map {
    NoteEntityAndSubEntity.transform(it)
  }.flowOn(Dispatchers.IO)

  fun addSubNote(data: SubNoteModel) {
    noteDao.addSubNote(SubNoteEntity.transform(data))
  }

  fun updateSubNote(data: SubNoteModel) {
    noteDao.updateSubNote(SubNoteEntity.transform(data))
  }

  fun deleteSubNote(data: SubNoteModel) {
    noteDao.deleteSubNote(SubNoteEntity.transform(data))
  }

  fun getAllSubNotes(idNote: Int): Flow<ArrayList<SubNoteModel>> =
    noteDao.getAllSubNotes(idNote).map {
      SubNoteEntity.transform(it)
    }.flowOn(Dispatchers.IO)

  fun updateSomeSubNote(idNote: Int) {
    noteDao.updateSomeSubsNotes(idNote)
  }

  fun getLatestNote(): Int {
    return noteDao.getLatestNote()
  }

  fun deleteNoteById(idNote: Int) {
    noteDao.deleteNoteById(idNote)
  }
}