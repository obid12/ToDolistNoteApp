package com.obidia.testagrii.domain.model

data class NoteModel(
  val id: Int,
  val activity: String,
  val detail: String,
  val category: String,
  val isFinish: Boolean
)