package com.obidia.testagrii.presentation.inputdata

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.obidia.testagrii.BuildConfig
import com.obidia.testagrii.R
import com.obidia.testagrii.databinding.FragmentInputDataBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.presentation.listnote.NoteViewModel
import com.obidia.testagrii.utils.error
import com.obidia.testagrii.utils.loading
import com.obidia.testagrii.utils.replaceIfNull
import com.obidia.testagrii.utils.success
import com.obidia.testagrii.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InputDataFragment : DialogFragment() {

  private val noteViewModel: NoteViewModel by viewModels()
  private val subNoteViewModel: SubNoteViewModel by viewModels()
  private var _binding: FragmentInputDataBinding? = null
  private var isUpdateNote: Boolean = false
  private var noteModel: NoteModel? = null
  private var subNoteAdapter: ListSubNotesAdapter = ListSubNotesAdapter()
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentInputDataBinding.inflate(inflater, container, false)
    loadArguments()
    setupObserver()
    setupView()
    return binding.root
  }

  private fun setupObserver() {
    lifecycleScope.launch {
      Log.d("KESINI", "id note ${noteModel?.id}")
      subNoteViewModel.getAllSubNote(noteModel?.id.replaceIfNull()).catch { }.collect { state ->
        state.loading { }
        state.success {
          setupAdapter(it)
        }
        state.error { }
      }
    }
  }

  private fun setupFloatButton() {
    binding.floatBtn.run {
      setOnClickListener {
        if (isUpdateNote) {
          noteModel?.let { data -> noteViewModel.deleteNote(data) }
          dismiss()
        } else insertDataToDatabase()
      }
      setImageDrawable(
        AppCompatResources.getDrawable(
          requireContext(),
          if (isUpdateNote) R.drawable.ic_delete
          else R.drawable.ic_add
        )
      )
    }
  }

  private fun loadArguments() {
    isUpdateNote = arguments?.getBoolean(ARGS_IS_UPDATE_NOTE).replaceIfNull()
    noteModel = arguments?.getParcelable(ARGS_DATA_NOTE)
  }

  private fun setupView() {
    setupRecycleView()
    adjustViewInput()
    setupInputSubNote()
    setupFloatButton()
  }

  private fun setupInputSubNote() {
    binding.etNoteBody.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        val data = SubNoteModel(
          0,
          if (isUpdateNote) noteModel?.id.replaceIfNull() else 0,
          v.text.toString(),
          isFinished = false
        )
        v.text = ""
        subNoteViewModel.addSubNote(data)
      }
      false
    }
  }

  private fun setupAdapter(list: ArrayList<SubNoteModel>) {
    subNoteAdapter.run {
      submitList(list)
    }
  }

  private fun setupRecycleView() {
    binding.rvSubNote.run {
      adapter = subNoteAdapter
      layoutManager = LinearLayoutManager(requireContext())
    }
  }

  private fun adjustViewInput() {
    binding.run {
      etNoteTitle.setText(if (isUpdateNote) noteModel?.activity else "")
      etKategory.setText(if (isUpdateNote) noteModel?.category else "")
      tvUpdateNote.let {
        it.visible(isUpdateNote)
        it.setOnClickListener {
          insertDataToDatabase()
        }
      }
    }
  }

  private fun insertDataToDatabase() {
    val title = binding.etNoteTitle.text.toString()
    val category = binding.etKategory.text.toString()

    if (inputCheck(title, category)) {
      val note = NoteModel(
        if (isUpdateNote) noteModel?.id.replaceIfNull() else 0,
        title,
        "",
        category,
        false
      )
      Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
      dialog?.dismiss()

      if (isUpdateNote) noteViewModel.updateNote(note)
      else noteViewModel.addNote(note)
      return
    }

    Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
  }

  private fun inputCheck(firstName: String, category: String): Boolean {
    return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(category))
  }

  companion object {
    private const val ARGS_DATA_NOTE = BuildConfig.APPLICATION_ID + "ARGS_DATA_NOTE"
    private const val ARGS_IS_UPDATE_NOTE = BuildConfig.APPLICATION_ID + "ARGS_IS_UPDATE_NOTE"

    fun newInstance(
      data: NoteModel? = null,
      isUpdateNote: Boolean = false
    ): InputDataFragment {
      val dialog = InputDataFragment()
      val bundle = Bundle()
      bundle.putParcelable(ARGS_DATA_NOTE, data)
      bundle.putBoolean(ARGS_IS_UPDATE_NOTE, isUpdateNote)
      dialog.arguments = bundle
      return dialog
    }
  }
}


