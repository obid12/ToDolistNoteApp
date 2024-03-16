package com.obidia.testagrii.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.obidia.testagrii.domain.model.SubNoteModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sub_note_table")
data class SubNoteEntity(
  @PrimaryKey(autoGenerate = true) val idSubNote: Int,
  val idNote: Int,
  val text: String,
  val isFinished: Boolean,
) : Parcelable {
  companion object {
    fun transform(entity: List<SubNoteEntity>): ArrayList<SubNoteModel> {
      return ArrayList(
        entity.map {
          SubNoteModel(
            it.idSubNote,
            it.idNote,
            it.text,
            it.isFinished,
          )
        },
      )
    }

    fun transform(entity: SubNoteModel): SubNoteEntity {
      return SubNoteEntity(
        entity.idSubNote,
        entity.idNote,
        entity.text,
        entity.isFinished,
      )
    }
  }
}
