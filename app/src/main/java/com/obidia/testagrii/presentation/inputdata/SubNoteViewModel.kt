package com.obidia.testagrii.presentation.inputdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.domain.usecase.NoteUseCase
import com.obidia.testagrii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class SubNoteViewModel @Inject constructor(
  private val useCase: NoteUseCase
) : ViewModel() {
  fun getAllSubNote(idNote: Int): Flow<Resource<ArrayList<SubNoteModel>>> =
    useCase.getAllSubNotes(idNote)

  fun addSubNote(data: SubNoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.addSubNote(data)
    }
  }

  fun updateSubNote(data: SubNoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.updateSubNote(data)
    }
  }

  fun deleteSubNote(data: SubNoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.deleteSubNote(data)
    }
  }
}