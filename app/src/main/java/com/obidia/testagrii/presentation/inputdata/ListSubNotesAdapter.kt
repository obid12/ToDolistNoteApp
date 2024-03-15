package com.obidia.testagrii.presentation.inputdata

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.obidia.testagrii.databinding.ItemSubNoteDetailPageBinding
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.utils.replaceIfNull
import com.obidia.testagrii.utils.visible

class ListSubNotesAdapter(val isListUser: Boolean = false) :
  ListAdapter<ListItemAdapter, ViewHolder>(DiffCallBack) {

  private var onDeleteListener: ((item: SubNoteModel?) -> Unit)? = null
  private var onCheckBoxListener: ((item: SubNoteModel?, position: Int) -> Unit)? = null
  private var onUpdateListener: ((item: SubNoteModel?, hasFocus: Boolean) -> Unit)? =
    null
  private var onAddItemListener: (() -> Unit)? = null

  fun setOnDeleteListener(listener: ((item: SubNoteModel?) -> Unit)?) {
    onDeleteListener = listener
  }

  fun setOnUpdateListener(listener: ((item: SubNoteModel?, hasFocus: Boolean) -> Unit)?) {
    onUpdateListener = listener
  }

  fun setOnCheckBoxListener(listener: ((item: SubNoteModel?, position: Int) -> Unit)?) {
    onCheckBoxListener = listener
  }

  fun setOnAddItemListener(listener: () -> Unit) {
    onAddItemListener = listener
  }

  fun updateItem(position: Int) {
    notifyItemChanged(position, Paint.STRIKE_THRU_TEXT_FLAG)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return if (viewType == SubNoteModelType.LIST.ordinal) {
      SubNoteViewHolder(
        ItemSubNoteDetailPageBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
        )
      )
    } else {
      ButtonSubNoteViewHolder(
        ItemSubNoteDetailPageBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
        )
      )
    }
  }

  override fun getItemViewType(position: Int): Int = getItem(position).viewType

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    when (data.viewType) {
      SubNoteModelType.LIST.ordinal -> {
        (holder as SubNoteViewHolder).bind(data)
      }

      SubNoteModelType.BUTTON.ordinal -> {
        (holder as ButtonSubNoteViewHolder).bind()
      }
    }
  }

  inner class ButtonSubNoteViewHolder(
    private var binding: ItemSubNoteDetailPageBinding
  ) : ViewHolder(binding.root) {
    fun bind() {
      binding.run {
        root.setOnClickListener {
          onAddItemListener?.invoke()
        }
        tvNoteBody.text = "Daftar Item"
        etNoteBody.visible(false)
        tvNoteBody.visible(true)
        btnCheckBox.visible(isVisible = false, true)
        icAdd.visible(true)
      }
    }
  }

  inner class SubNoteViewHolder(
    private var binding: ItemSubNoteDetailPageBinding
  ) : ViewHolder(binding.root) {
    fun bind(item: ListItemAdapter) {
      val data = item.item
      binding.run {
        btnCheckBox.let { check ->
          check.isEnabled = !isListUser
          check.setOnClickListener {
            data?.isFinished = !data?.isFinished.replaceIfNull()
            onCheckBoxListener?.invoke(data, adapterPosition)
          }
          check.isChecked = data?.isFinished.replaceIfNull()
        }
        tvNoteBody.let {
          it.text = data?.text
          it.visible(isListUser)
          it.paintFlags =
            if (data?.isFinished.replaceIfNull()) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
        }
        ivDelete.let {
          it.setOnClickListener {
            onDeleteListener?.invoke(data)
          }
        }
        etNoteBody.let {
          it.visible(!isListUser)
          it.setText(data?.text)
          it.paintFlags = if (data?.isFinished.replaceIfNull())
            Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
          it.onFocusChangeListener = getFocusWatcher(it, data, ivDelete)
          it.addTextChangedListener(getTextWatcher(it, data))
        }
      }
    }
  }

  private fun getFocusWatcher(
    editText: EditText,
    item: SubNoteModel?,
    img: ImageView
  ): View.OnFocusChangeListener {
    return View.OnFocusChangeListener { _, _ ->
      img.visible(!isListUser && editText.hasFocus())
      if (editText.hasFocus()) return@OnFocusChangeListener
      item?.text = editText.text.toString()
      onUpdateListener?.invoke(item, editText.hasFocus())
    }
  }

  private fun getTextWatcher(editText: EditText, data: SubNoteModel?): TextWatcher {
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
          data?.text = editText.text.toString()
          onUpdateListener?.invoke(data, editText.hasFocus())
        }
      }
    }
  }

  object DiffCallBack : DiffUtil.ItemCallback<ListItemAdapter>() {
    override fun areItemsTheSame(
      oldItem: ListItemAdapter,
      newItem: ListItemAdapter
    ): Boolean = oldItem.item?.idSubNote == newItem.item?.idSubNote

    override fun areContentsTheSame(
      oldItem: ListItemAdapter,
      newItem: ListItemAdapter
    ): Boolean = oldItem.hashCode() == newItem.hashCode()
  }
}