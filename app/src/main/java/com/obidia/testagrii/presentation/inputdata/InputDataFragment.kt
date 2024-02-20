package com.obidia.testagrii.presentation.inputdata

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InputDataFragment : BottomSheetDialogFragment() {

  private val noteViewModel: NoteViewModel by viewModels()
  private val subNoteViewModel: SubNoteViewModel by viewModels()
  private var _binding: FragmentInputDataBinding? = null
  private var isUpdateNote: Boolean = false
  private var noteModel: NoteModel? = null
  private var subNoteAdapter: ListSubNotesAdapter = ListSubNotesAdapter()
  private val binding get() = _binding!!

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = BottomSheetDialog(requireContext(), theme)
    dialog.setShowDialog()
    return dialog
  }

  private fun BottomSheetDialog.setShowDialog() {
    this.setOnShowListener {
      val bottomSheetDialog = it as BottomSheetDialog
      val parentLayout =
        bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
      parentLayout?.let { parent ->
        val behaviour = BottomSheetBehavior.from(parent)
        setupFullHeight(parent)
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  private fun setupFullHeight(bottomSheet: View) {
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    bottomSheet.layoutParams = layoutParams
  }

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
    binding.etNoteBody.setOnEditorActionListener { v, actionId, event ->
      if (actionId == 0) {

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
      setOnDeleteListener {
        subNoteViewModel.deleteSubNote(it)
      }
      setOnUpdateListener { item, text ->
        subNoteViewModel.updateSubNote(item.apply {
          this.text = text
        })
      }
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
    val note = NoteModel(
      if (isUpdateNote) noteModel?.id.replaceIfNull() else 0,
      title,
      "",
      "",
      false
    )
    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
    dialog?.dismiss()

    if (isUpdateNote) noteViewModel.updateNote(note)
    else noteViewModel.addNote(note)
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


