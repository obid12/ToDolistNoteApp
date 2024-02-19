package com.obidia.testagrii.presentation.inputdata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obidia.testagrii.databinding.ItemSubNoteBinding
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.presentation.inputdata.ListSubNotesAdapter.SubNoteViewHolder

class ListSubNotesAdapter : ListAdapter<SubNoteModel, SubNoteViewHolder>(DiffCallBack) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubNoteViewHolder {
    return SubNoteViewHolder(
      ItemSubNoteBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: SubNoteViewHolder, position: Int) {
    val data = getItem(position)
    holder.bind(data)
  }

  inner class SubNoteViewHolder(
    private var binding: ItemSubNoteBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SubNoteModel) {
      binding.tvSubNote.text = data.text
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