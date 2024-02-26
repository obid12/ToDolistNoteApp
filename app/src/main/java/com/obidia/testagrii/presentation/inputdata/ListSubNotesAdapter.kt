package com.obidia.testagrii.presentation.inputdata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.obidia.testagrii.databinding.ItemSubNoteBinding
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.presentation.inputdata.ListSubNotesAdapter.SubNoteViewHolder
import com.obidia.testagrii.utils.visible

class ListSubNotesAdapter(val isListUser: Boolean = false) :
  ListAdapter<SubNoteModel, SubNoteViewHolder>(DiffCallBack) {

  private var onDeleteListener: ((item: SubNoteModel) -> Unit)? = null
  private var onUpdateListener: ((item: SubNoteModel, text: String) -> Unit)? = null

  fun setOnDeleteListener(listener: ((item: SubNoteModel) -> Unit)?) {
    onDeleteListener = listener
  }

  fun setOnUpdateListener(listener: ((item: SubNoteModel, text: String) -> Unit)?) {
    onUpdateListener = listener
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubNoteViewHolder {
    val view = ItemSubNoteBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    )
//    if (isListUser) {
//      view.root.isFocusable = false
//      view.root.isFocusableInTouchMode = false
//      view.root.requestFocus()
//      parent.isFocusable = false
//      parent.isFocusableInTouchMode = false
//      parent.requestFocus()
//    }
    return SubNoteViewHolder(
      view
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
      binding.run {
        btnCheckBox.isEnabled = !isListUser
        tvNoteBody.let {
          it.text = data.text
          it.visible(isListUser)
        }
        etNoteBody.let {
          it.visible(!isListUser)
          it.setText(data.text)
          it.onFocusChangeListener = getFocusWatcher(it, data)
        }
        ivDelete.let {
          it.setOnClickListener {
            onDeleteListener?.invoke(data)
          }
          it.visible(!isListUser)
        }
      }
    }
  }

  private fun getFocusWatcher(editText: EditText, item: SubNoteModel): View.OnFocusChangeListener {
    return View.OnFocusChangeListener { _, _ ->
      onUpdateListener?.invoke(item, editText.text.toString())
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