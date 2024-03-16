package com.obidia.testagrii.presentation.inputdata

import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import javax.inject.Inject

class InputDataModel
  @Inject
  constructor() {
    var isUpdateNote: Boolean = false
    var noteModel: NoteModel? = null
    var listSubNote: ArrayList<SubNoteModel> = arrayListOf()
    var title: String = ""
    var idNote: Int = 0
    var dataSubNoteModel: SubNoteModel? = null
    val listUpdate: ArrayList<SubNoteModel> = arrayListOf()
  }

data class ListItemAdapter(
  val viewType: Int,
  val item: SubNoteModel? = null,
) {
  companion object {
    fun transform(listData: ArrayList<SubNoteModel>): ArrayList<ListItemAdapter> {
      val result: ArrayList<ListItemAdapter> = arrayListOf()
      listData.forEach {
        result.add(ListItemAdapter(SubNoteModelType.LIST.ordinal, it))
      }
      result.add(ListItemAdapter(SubNoteModelType.BUTTON.ordinal))
      return result
    }
  }
}

enum class SubNoteModelType {
  LIST,
  BUTTON,
}
