package com.obidia.testagrii.presentation.listnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.usecase.NoteUseCase
import com.obidia.testagrii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel
  @Inject
  constructor(
    private val useCase: NoteUseCase,
  ) : ViewModel() {
    fun getAllNote(): Flow<Resource<ArrayList<NoteAndSubNoteModel>>> = useCase.getAllNotes()

    fun addNote(data: NoteModel) {
      viewModelScope.launch(Dispatchers.IO) {
        useCase.addNote(data)
      }
    }

    fun deleteNoteById(noteId: Int) {
      viewModelScope.launch(Dispatchers.IO) {
        useCase.deleteNoteById(noteId)
      }
    }

    fun updateNote(
      title: String,
      idNote: Int,
    ) {
      viewModelScope.launch(Dispatchers.IO) {
        useCase.updateNote(
          NoteModel(
            idNote,
            title,
            "",
            "",
            false,
          ),
        )
      }
    }
  }
