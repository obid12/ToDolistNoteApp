package com.obidia.testagrii.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(
  val id: Int,
  val activity: String,
  val detail: String,
  val category: String,
  var isFinish: Boolean,
) : Parcelable
