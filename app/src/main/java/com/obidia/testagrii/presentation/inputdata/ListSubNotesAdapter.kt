package com.obidia.testagrii.presentation.inputdata

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
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
  private var onCheckBoxListener: ((item: SubNoteModel, position: Int) -> Unit)? = null
  private var onUpdateListener: ((item: SubNoteModel, text: String, hasFocus: Boolean) -> Unit)? =
    null

  fun setOnDeleteListener(listener: ((item: SubNoteModel) -> Unit)?) {
    onDeleteListener = listener
  }

  fun setOnUpdateListener(listener: ((item: SubNoteModel, text: String, hasFocus: Boolean) -> Unit)?) {
    onUpdateListener = listener
  }

  fun setOnCheckBoxListener(listener: ((item: SubNoteModel, position: Int) -> Unit)?) {
    onCheckBoxListener = listener
  }

  fun updateItem(position: Int) {
    notifyItemChanged(position, Paint.STRIKE_THRU_TEXT_FLAG)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubNoteViewHolder {
    val view = ItemSubNoteBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    )
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
        btnCheckBox.let { check ->
          check.isEnabled = !isListUser
          check.setOnClickListener {
            data.isFinished = !data.isFinished
            onCheckBoxListener?.invoke(data, adapterPosition)
          }
          check.isChecked = data.isFinished
        }
        tvNoteBody.let {
          it.text = data.text
          it.visible(isListUser)
          it.paintFlags = if (data.isFinished) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
        }
        ivDelete.let {
          it.setOnClickListener {
            onDeleteListener?.invoke(data)
          }
        }
        etNoteBody.let {
          it.visible(!isListUser)
          it.setText(data.text)
          it.paintFlags = if (data.isFinished) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
          it.onFocusChangeListener = getFocusWatcher(it, data, ivDelete)
          it.addTextChangedListener(getTextWatcher(it, data))
        }
      }
    }
  }

  private fun getFocusWatcher(
    editText: EditText,
    item: SubNoteModel,
    img: ImageView
  ): View.OnFocusChangeListener {
    return View.OnFocusChangeListener { _, _ ->
      img.visible(!isListUser && editText.hasFocus())
      if (editText.hasFocus()) return@OnFocusChangeListener
      onUpdateListener?.invoke(item, editText.text.toString(), editText.hasFocus())
    }
  }

  private fun getTextWatcher(editText: EditText, data: SubNoteModel): TextWatcher {
    var oldText = ""
    var isChange = false
    return object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        oldText = p0.toString()
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        isChange = oldText != p0.toString()
      }

      override fun afterTextChanged(p0: Editable?) {
        if (editText.hasFocus() && isChange) {
          onUpdateListener?.invoke(data, editText.text.toString(), editText.hasFocus())
        }
      }

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