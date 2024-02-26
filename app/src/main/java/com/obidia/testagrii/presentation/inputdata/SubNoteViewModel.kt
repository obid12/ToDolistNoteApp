package com.obidia.testagrii.presentation.inputdata

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.domain.usecase.NoteUseCase
import com.obidia.testagrii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class SubNoteViewModel @Inject constructor(
  private val useCase: NoteUseCase
) : ViewModel() {

  private var isUpdate: Boolean = false
  private var idNoteUpdate: Int = 0

  private val _listSubNote: MutableStateFlow<Resource<ArrayList<SubNoteModel>>> = MutableStateFlow(
    Resource.Success(arrayListOf())
  )
  val listSUbNote get() = _listSubNote

  private val _idNote: MutableStateFlow<Int?> = MutableStateFlow(null)
  val idNote get() = _idNote

  fun setIsUpdate(isUpdate: Boolean, idNote: Int) {
    this.isUpdate = isUpdate
    this.idNoteUpdate = idNote
  }

  fun getAllSubNote() {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.getAllSubNotes(
        if (isUpdate) idNoteUpdate
        else getLatestNote()
      ).catch { }.collect {
        listSUbNote.value = it
      }
    }
  }

  fun deleteSubNote(data: SubNoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.deleteSubNote(data)
    }
  }

  fun updateSubNote(text: String, idSubNote: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.updateSubNote(
        SubNoteModel(
          idSubNote,
          if (isUpdate) idNoteUpdate else getLatestNote(),
          text, false
        )
      )
    }
  }

  fun addSubNote(text: String) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.addSubNote(
        SubNoteModel(
          0,
          if (isUpdate) idNoteUpdate else getLatestNote(),
          text, false
        )
      )
    }
  }

  fun getNoteId() {
    viewModelScope.launch(Dispatchers.IO) {
      _idNote.value = getLatestNote()
    }
  }

  private fun getLatestNote(): Int = useCase.getLatestNote()
}