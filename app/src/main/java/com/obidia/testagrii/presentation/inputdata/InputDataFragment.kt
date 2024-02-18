package com.obidia.testagrii.presentation.inputdata

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.obidia.testagrii.databinding.FragmentInputDataBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.listnote.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputDataFragment : DialogFragment() {

  private val noteViewModel: NoteViewModel by viewModels()
  private var _binding: FragmentInputDataBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentInputDataBinding.inflate(inflater, container, false)
    setupView()
    return binding.root
  }

  private fun setupView() {
    binding.floatBtn.setOnClickListener {
      insertDataToDatabase()
    }
  }

  private fun insertDataToDatabase() {
    val title = binding.etNoteTitle.text.toString()
    val category = binding.etKategory.text.toString()
    val detail = binding.etNoteBody.text.toString()


    if (inputCheck(title, category, detail)) {
      val user = NoteModel(0, title, detail, category, false)
      noteViewModel.addNote(user)
      Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
      dialog?.dismiss()
      return
    }

    Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
  }

  private fun inputCheck(firstName: String, lastName: String, category: String): Boolean {
    return !(TextUtils.isEmpty(firstName) &&
      TextUtils.isEmpty(lastName) && TextUtils.isEmpty(category))
  }

}


