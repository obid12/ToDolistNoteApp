package com.obidia.testagrii.domain.repo

import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
  fun addNote(data: NoteModel)
  fun updateNote(data: NoteModel)
  fun deleteNote(data: MutableList<NoteModel>)
  fun deleteAllNotes()
  fun getAllNotes(): Flow<Resource<ArrayList<NoteAndSubNoteModel>>>
  fun addSubNote(data: SubNoteModel)
  fun updateSubNote(data: SubNoteModel)
  fun deleteSubNote(data: SubNoteModel)
  fun getAllSubNotes(idNote: Int): Flow<Resource<ArrayList<SubNoteModel>>>
  fun updateSomeSubNote(idNote: Int)
  fun getLatestNote(): Int
  fun deleteNoteById(noteId: Int)
}