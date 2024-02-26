package com.obidia.testagrii.data.repository

import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.domain.repo.NoteRepository
import com.obidia.testagrii.utils.Resource
import com.obidia.testagrii.utils.Resource.Loading
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class NoteRepositoryImplementation @Inject constructor(
  private val localDataSource: LocalDataSource
) : NoteRepository {
  override fun addNote(data: NoteModel) {
    localDataSource.addNote(data)
  }

  override fun updateNote(data: NoteModel) {
    localDataSource.updateNote(data)
  }

  override fun deleteNote(data: NoteModel) {
    localDataSource.deleteNote(data)
  }

  override fun deleteAllNotes() {
    localDataSource.deleteAllNotes()
  }

  override fun getAllNotes(): Flow<Resource<ArrayList<NoteAndSubNoteModel>>> {
    return flow {
      val data = localDataSource.getAllNotes()
      data.onStart { emit(Loading) }.catch {
        emit(Resource.Error(it))
      }.collect {
        emit(Resource.Success(it))
      }
    }
  }

  override fun addSubNote(data: SubNoteModel) {
    localDataSource.addSubNote(data)
  }

  override fun updateSubNote(data: SubNoteModel) {
    localDataSource.updateSubNote(data)
  }

  override fun deleteSubNote(data: SubNoteModel) {
    localDataSource.deleteSubNote(data)
  }

  override fun getAllSubNotes(idNote: Int): Flow<Resource<ArrayList<SubNoteModel>>> {
    return flow {
      val data = localDataSource.getAllSubNotes(idNote)
      data.onStart { emit(Loading) }.catch {
        emit(Resource.Error(it))
      }.collect {
        emit(Resource.Success(it))
      }
    }
  }

  override fun updateSomeSubNote(idNote: Int) {
    localDataSource.updateSomeSubNote(idNote)
  }

  override fun getLatestNote(): Int = localDataSource.getLatestNote()

  override fun deleteNoteById(noteId: Int) {
    localDataSource.deleteNoteById(noteId)
  }
}