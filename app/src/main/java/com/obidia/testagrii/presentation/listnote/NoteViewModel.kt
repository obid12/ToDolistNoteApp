package com.obidia.testagrii.presentation.listnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.usecase.NoteUseCase
import com.obidia.testagrii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(
  private val useCase: NoteUseCase
) : ViewModel() {
  private val _isFinish = MutableLiveData<Boolean>()
  val isFinish get() = _isFinish

  fun setFinish(data: Boolean) {
    _isFinish.value = data
  }

  fun getAllNote(): Flow<Resource<ArrayList<NoteModel>>> = useCase.getAllNotes()

  fun addNote(data: NoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.addNote(data)
    }
  }

  fun updateUser(data: NoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.updateNote(data)
    }
  }

  fun deleteNote(data: NoteModel) {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.deleteNote(data)
    }
  }

  fun deleteAllUsers() {
    viewModelScope.launch(Dispatchers.IO) {
      useCase.deleteAllNotes()
    }
  }

}