package com.obidia.testagrii.presentation.inputdata

import android.text.Editable
import android.text.TextWatcher
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

class ListSubNotesAdapter : ListAdapter<SubNoteModel, SubNoteViewHolder>(DiffCallBack) {

  private var onDeleteListener: ((item: SubNoteModel) -> Unit)? = null
  private var onUpdateListener: ((item: SubNoteModel, text: String) -> Unit)? = null

  fun setOnDeleteListener(listener: ((item: SubNoteModel) -> Unit)?) {
    onDeleteListener = listener
  }

  fun setOnUpdateListener(listener: ((item: SubNoteModel, text: String) -> Unit)?) {
    onUpdateListener = listener
  }

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
      binding.run {
        etNoteBody.let {
          it.setText(data.text)
//          it.onFocusChangeListener = getFocusWatcher(it, data)
          it.addTextChangedListener(getCharacterWatcher(it, data))
        }
        ivDelete.setOnClickListener {
          onDeleteListener?.invoke(data)
        }
      }
    }
  }

  private fun getCharacterWatcher(editText: EditText, item: SubNoteModel): TextWatcher {
    return object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
      override fun afterTextChanged(p0: Editable?) {
        onUpdateListener?.invoke(item, editText.text.toString())
      }
    }
  }

//  private fun getFocusWatcher(editText: EditText, item: SubNoteModel): View.OnFocusChangeListener {
//    return View.OnFocusChangeListener { _, _ ->
//      if (!editText.hasFocus())
//        onUpdateListener?.invoke(item, editText.text.toString())
//    }
//  }

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