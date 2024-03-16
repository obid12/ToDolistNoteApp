package com.obidia.testagrii.domain.model

data class NoteAndSubNoteModel(
  val noteEntity: NoteModel,
  val listSubNoteEntity: ArrayList<SubNoteModel>,
)
