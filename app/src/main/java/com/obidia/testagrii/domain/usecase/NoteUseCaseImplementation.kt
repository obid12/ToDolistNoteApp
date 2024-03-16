package com.obidia.testagrii.domain.usecase

import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.domain.repo.NoteRepository
import com.obidia.testagrii.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class NoteUseCaseImplementation
  @Inject
  constructor(
    private val repository: NoteRepository,
  ) : NoteUseCase {
    override fun addNote(data: NoteModel) {
      repository.addNote(data)
    }

    override fun updateNote(data: NoteModel) {
      repository.updateNote(data)
    }

    override fun deleteNote(data: NoteModel) {
      repository.deleteNote(data)
    }

    override fun deleteAllNotes() {
      repository.deleteAllNotes()
    }

    override fun getAllNotes(): Flow<Resource<ArrayList<NoteAndSubNoteModel>>> {
      return repository.getAllNotes()
    }

    override fun addSubNote(data: SubNoteModel) {
      repository.addSubNote(data)
    }

    override fun updateSubNote(data: SubNoteModel) {
      repository.updateSubNote(data)
    }

    override fun deleteSubNote(data: SubNoteModel) {
      repository.deleteSubNote(data)
    }

    override fun getAllSubNotes(idNote: Int): Flow<Resource<ArrayList<SubNoteModel>>> {
      return repository.getAllSubNotes(idNote)
    }

    override fun updateSomeSubNote(idNote: Int) {
      repository.updateSomeSubNote(idNote)
    }

    override fun getLatestNote(): Int = repository.getLatestNote()

    override fun deleteNoteById(noteId: Int) {
      repository.deleteNoteById(noteId)
    }
  }
