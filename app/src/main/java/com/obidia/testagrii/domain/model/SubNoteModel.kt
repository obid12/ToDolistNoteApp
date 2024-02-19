package com.obidia.testagrii.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubNoteModel(
  val idSubNote: Int,
  val idNote: Int,
  val text: String,
  val isFinished: Boolean
) : Parcelable