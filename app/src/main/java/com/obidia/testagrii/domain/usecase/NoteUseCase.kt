package com.obidia.testagrii.domain.usecase

import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NoteUseCase {
  fun addNote(data: NoteModel)
  fun updateNote(data: NoteModel)
  fun deleteNote(data: NoteModel)
  fun deleteAllNotes()
  fun getAllNotes(): Flow<Resource<ArrayList<NoteModel>>>
  fun getNoteByCategory(key: String): Flow<Resource<ArrayList<NoteModel>>>
}