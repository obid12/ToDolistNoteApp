package com.obidia.testagrii.presentation.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.obidia.testagrii.R
import com.obidia.testagrii.databinding.FragmentUpdateDataBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.listnote.NoteViewModel
import com.obidia.testagrii.utils.replaceIfNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateDataFragment(private val noteEntity: NoteModel) : DialogFragment() {

  private val noteViewModel: NoteViewModel by viewModels()
  private var _binding: FragmentUpdateDataBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {

    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentUpdateDataBinding.inflate(inflater, container, false)

    binding.floatBtnDelete.setOnClickListener {

      val builder = AlertDialog.Builder(requireContext())
      builder.setPositiveButton("Yes") { _, _ ->
        Log.d("KESINI","data $noteEntity")
        noteViewModel.deleteNote(noteEntity)
        Toast.makeText(
          requireContext(),
          "Successfully removed: ${noteEntity.activity}",
          Toast.LENGTH_SHORT
        ).show()
        dialog?.dismiss()

      }
      builder.setNegativeButton("No") { _, _ -> }
      builder.setTitle("Delete ${noteEntity?.activity}?")
      builder.setMessage("Are you sure you want to delete ${noteEntity?.activity}?")
      builder.create().show()

    }
    binding.floatBtn.setOnClickListener {
      updateDataToDatabase()
    }

    binding.selesai.setOnClickListener {
      if (noteEntity?.isFinish == true) {
        noteViewModel.setFinish(false)
        noteViewModel.updateUser(
          NoteModel(
            noteEntity.id,
            noteEntity.activity,
            noteEntity.detail,
            noteEntity.category,
            noteViewModel.isFinish.value.replaceIfNull()
          )
        )
      } else {
        noteViewModel.setFinish(true)
        noteViewModel.updateUser(
          NoteModel(
            noteEntity?.id.replaceIfNull(),
            noteEntity?.activity.replaceIfNull(),
            noteEntity?.detail.replaceIfNull(),
            noteEntity?.category.replaceIfNull(),
            noteViewModel.isFinish.value.replaceIfNull()
          )
        )
      }
      dialog?.dismiss()

    }

    binding.selesai.apply {
      if (noteEntity?.isFinish == true) {
        text = "Telah dikerjakan"
        setTextColor(resources.getColor(R.color.teal_200))
      } else {
        text = "tandai selesai"
        setTextColor(resources.getColor(R.color.purple_200))
      }
    }


    binding.etNoteTitle.setText(noteEntity?.activity)
    binding.etNoteBody.setText(noteEntity?.detail)
    binding.etKategory.setText(noteEntity?.category)
    return binding.root
  }

  private fun updateDataToDatabase() {
    val title = binding.etNoteTitle.text.toString()
    val category = binding.etKategory.text.toString()
    val detail = binding.etNoteBody.text.toString()

    if (inputCheck(title, category, detail)) {
      val updatedUser = NoteModel(noteEntity?.id!!, title, detail, category, noteEntity.isFinish)
      noteViewModel.updateUser(updatedUser)
      Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
      dialog?.dismiss()
      // Navigate Back
    } else {
      Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
    }
  }

  private fun inputCheck(firstName: String, lastName: String, catgeory: String): Boolean {
    return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(
      catgeory
    ))
  }

}