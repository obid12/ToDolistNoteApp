package com.obidia.testagrii.presentation.listnote

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.obidia.testagrii.databinding.ItemSubNoteBinding
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.presentation.listnote.ListSubNoteAdapter.SubNoteViewHolder
import com.obidia.testagrii.utils.replaceIfNull

class ListSubNoteAdapter : ListAdapter<SubNoteModel, SubNoteViewHolder>(DiffCallBack) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubNoteViewHolder {
    return SubNoteViewHolder(
      ItemSubNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun onBindViewHolder(holder: SubNoteViewHolder, position: Int) {
    val data = getItem(position)
    holder.bind(data)
  }

  inner class SubNoteViewHolder(
    private var binding: ItemSubNoteBinding
  ) : ViewHolder(binding.root) {
    fun bind(data: SubNoteModel) {
      binding.tvNoteBody.text = data.text
      binding.tvNoteBody.paintFlags =
        if (data.isFinished) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
      binding.btnCheckBox.isChecked = data.isFinished.replaceIfNull()
    }
  }

  object DiffCallBack : DiffUtil.ItemCallback<SubNoteModel>() {
    override fun areItemsTheSame(
      oldItem: SubNoteModel,
      newItem: SubNoteModel
    ): Boolean = oldItem.idSubNote == newItem.idSubNote

    override fun areContentsTheSame(
      oldItem: SubNoteModel,
      newItem: SubNoteModel
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
  }
}