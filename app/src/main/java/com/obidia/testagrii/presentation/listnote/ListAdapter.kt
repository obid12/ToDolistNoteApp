package com.obidia.testagrii.presentation.listnote

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obidia.testagrii.databinding.ItemNoteBinding
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.presentation.inputdata.ListSubNotesAdapter
import com.obidia.testagrii.presentation.listnote.ListAdapter.ListViewHolder

class ListAdapter(val isListUser: Boolean = false) :
  ListAdapter<NoteAndSubNoteModel, ListViewHolder>(DiffCallBack) {

  private var onClickItem: ((data: NoteAndSubNoteModel) -> Unit)? = null
  private var onClickFinish: ((data: NoteAndSubNoteModel, position: Int) -> Unit)? = null

  fun setOnClickItem(listener: ((data: NoteAndSubNoteModel) -> Unit)?) {
    onClickItem = listener
  }

  fun setOnClickFinish(listener: ((data: NoteAndSubNoteModel, position: Int) -> Unit)?) {
    onClickFinish = listener
  }

  fun updateItem(position: Int) {
    notifyItemChanged(position, Color.GREEN)
  }

  inner class ListViewHolder(
    private var binding: ItemNoteBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    private val subNoteAdapter: ListSubNotesAdapter = ListSubNotesAdapter(isListUser)
    private fun getContext(): Context = itemView.context

    fun bind(data: NoteAndSubNoteModel) {
      binding.run {
        dataEntity = data
        cardView.setOnClickListener {
          onClickItem?.invoke(data)
        }
        rvSubNote.let {
          it.adapter = subNoteAdapter
          it.layoutManager = LinearLayoutManager(getContext())
          it.onTouchListener()
        }
        subNoteAdapter.submitList(data.listSubNoteEntity)
        binding.rvSubNote
        executePendingBindings()
      }
    }

    private fun RecyclerView.onTouchListener() {
      this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
          if (e.action == MotionEvent.ACTION_DOWN) {
            binding.cardView.performClick()
          }
          return false
        }
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
      })
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

  object DiffCallBack : DiffUtil.ItemCallback<NoteAndSubNoteModel>() {
    override fun areItemsTheSame(
      oldItem: NoteAndSubNoteModel,
      newItem: NoteAndSubNoteModel
    ): Boolean = oldItem.noteEntity.id == newItem.noteEntity.id

    override fun areContentsTheSame(
      oldItem: NoteAndSubNoteModel,
      newItem: NoteAndSubNoteModel
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
  }
}