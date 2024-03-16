package com.obidia.testagrii.data.local.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteEntityAndSubEntity(
  @Embedded
  val noteEntity: NoteEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "idNote",
  )
  val listSubNoteEntity: List<SubNoteEntity>,
) : Parcelable {
  companion object {
    fun transform(list: List<NoteEntityAndSubEntity>): ArrayList<NoteAndSubNoteModel> {
      return ArrayList(
        list.map {
          NoteAndSubNoteModel(
            NoteEntity.transform(it.noteEntity),
            SubNoteEntity.transform(it.listSubNoteEntity),
          )
        },
      )
    }
  }
}
