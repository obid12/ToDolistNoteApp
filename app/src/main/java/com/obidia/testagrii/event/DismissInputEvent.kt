package com.obidia.testagrii.event

import com.obidia.testagrii.domain.model.SubNoteModel

class DismissInputEvent(
  val listSubNoteModel: ArrayList<SubNoteModel>,
  val idNote: Int,
  val title: String
)