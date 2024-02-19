package com.obidia.testagrii.presentation.listnote

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obidia.testagrii.databinding.ItemNoteBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.listnote.ListAdapter.ListViewHolder

class ListAdapter : ListAdapter<NoteModel, ListViewHolder>(DiffCallBack) {

  private var onClickItem: ((data: NoteModel) -> Unit)? = null
  private var onClickFinish: ((data: NoteModel) -> Unit)? = null

  fun setOnClickItem(listener: ((data: NoteModel) -> Unit)?) {
    onClickItem = listener
  }

  fun setOnClickFinish(listener: ((data: NoteModel) -> Unit)?) {
    onClickFinish = listener
    notifyItemRangeChanged(0, itemCount)
  }

  inner class ListViewHolder(
    private var binding: ItemNoteBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NoteModel) {
      binding.run {
        dataEntity = data
        root.setOnClickListener {
          onClickItem?.invoke(data)
        }
        cardView.setBackgroundColor(if (data.isFinish) Color.GREEN else Color.WHITE)
        selesai.let {
          it.setOnClickListener {
            onClickFinish?.invoke(data)
          }
          it.setTextColor(
            if (data.isFinish) Color.BLACK
            else Color.parseColor("#FFBB86FC")
          )
          it.text = if (data.isFinish) "Telah Dikerjakan" else "Tandai Selesai"
        }
        executePendingBindings()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
    return ListViewHolder(
      ItemNoteBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    val data = getItem(position)
    holder.bind(data)
  }

  object DiffCallBack : DiffUtil.ItemCallback<NoteModel>() {
    override fun areItemsTheSame(
      oldItem: NoteModel,
      newItem: NoteModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
      oldItem: NoteModel,
      newItem: NoteModel
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
  }
}