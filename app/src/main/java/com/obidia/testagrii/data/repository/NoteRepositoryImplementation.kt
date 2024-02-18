package com.obidia.testagrii.data.repository

import com.obidia.testagrii.domain.model.NoteModel
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

  override fun getAllNotes(): Flow<Resource<ArrayList<NoteModel>>> {
    return flow {
      val data = localDataSource.getAllNotes()
      data.onStart { emit(Loading) }.catch {
        emit(Resource.Error(it))
      }.collect {
        emit(Resource.Success(it))
      }
    }
  }

  override fun getNoteByCategory(key: String): Flow<Resource<ArrayList<NoteModel>>> {
    return flow {
      val data = localDataSource.getNoteByCategory(key)
      data.onStart { emit(Loading) }.catch {
        emit(Resource.Error(it))
      }.collect {
        emit(Resource.Success(it))
      }
    }
  }
}