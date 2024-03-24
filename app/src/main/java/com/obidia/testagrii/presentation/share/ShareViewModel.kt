package com.obidia.testagrii.presentation.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obidia.testagrii.event.DismissInputEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
) : ViewModel() {
  private val _share: MutableLiveData<DismissInputEvent> = MutableLiveData()
  val share get() = _share

  fun saveShare(data: DismissInputEvent) {
    _share.value = data
  }
}