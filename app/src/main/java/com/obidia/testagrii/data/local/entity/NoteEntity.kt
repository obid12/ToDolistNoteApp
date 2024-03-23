package com.obidia.testagrii.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.utils.replaceIfNull
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note_table")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Int,
  val activity: String?,
  val detail: String?,
  val category: String?,
  val isFinish: Boolean?
) : Parcelable {
  companion object {

    fun transform(entity: List<NoteModel>): ArrayList<NoteEntity> = ArrayList(
      entity.map {
        NoteEntity(
          it.id.replaceIfNull(),
          it.activity.replaceIfNull(),
          it.detail.replaceIfNull(),
          it.category.replaceIfNull(),
          it.isFinish.replaceIfNull()
        )
      }
    )

    fun transform(model: NoteModel): NoteEntity = NoteEntity(
      model.id,
      model.activity,
      model.detail,
      model.category,
      model.isFinish
    )

    fun transform(model: NoteEntity): NoteModel = NoteModel(
      model.id,
      model.activity.replaceIfNull(),
      model.detail.replaceIfNull(),
      model.category.replaceIfNull(),
      model.isFinish.replaceIfNull()
    )
  }
}
