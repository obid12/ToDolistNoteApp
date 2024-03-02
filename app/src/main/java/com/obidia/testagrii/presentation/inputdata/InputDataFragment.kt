package com.obidia.testagrii.presentation.inputdata

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.obidia.testagrii.BuildConfig
import com.obidia.testagrii.databinding.FragmentInputDataBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.utils.error
import com.obidia.testagrii.utils.loading
import com.obidia.testagrii.utils.replaceIfNull
import com.obidia.testagrii.utils.success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InputDataFragment : BottomSheetDialogFragment() {

  private val subNoteViewModel: SubNoteViewModel by viewModels()
  private var _binding: FragmentInputDataBinding? = null
  private var isUpdateNote: Boolean = false
  private var noteModel: NoteModel? = null
  private var subNoteAdapter: ListSubNotesAdapter = ListSubNotesAdapter()
  private var listSubNote: ArrayList<SubNoteModel> = arrayListOf()
  private var title: String = ""
  private var idNote: Int = 0
  private var idSubNote: Int = 0
  private var textSubNote: String = ""
  private val binding get() = _binding!!
  private var onDisMissListener: ((listSubNote: ArrayList<SubNoteModel>, idNote: Int, title: String) -> Unit)? =
    null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = BottomSheetDialog(requireContext(), theme)
    dialog.setShowDialog()
    return dialog
  }

  fun setOnDisMissListener(listener: ((listSubNote: ArrayList<SubNoteModel>, idNote: Int, title: String) -> Unit)? = null) {
    onDisMissListener = listener
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
    setIsUpdate()
    setupObserver()
    setupView()
    return binding.root
  }

  private fun setIsUpdate() {
    subNoteViewModel.setIsUpdate(isUpdateNote, noteModel?.id.replaceIfNull())
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    title = binding.etNoteTitle.text.toString()
    subNoteViewModel.updateSubNote(textSubNote, idSubNote)
    onDisMissListener?.invoke(listSubNote, getNoteId(), title)
  }

  private fun setupObserver() {
    getNoteIdObserver()
    getListNoteObserver()
  }

  private fun getNoteIdObserver() {
    subNoteViewModel.getNoteId()
    lifecycleScope.launch {
      subNoteViewModel.idNote.flowWithLifecycle(lifecycle).catch { }
        .collect {
          idNote = it.replaceIfNull()
        }
    }
  }

  private fun getNoteId(): Int {
    return if (isUpdateNote) noteModel?.id.replaceIfNull()
    else idNote
  }

  private fun getListNoteObserver() {
    subNoteViewModel.getAllSubNote()
    lifecycleScope.launch {
      subNoteViewModel.listSUbNote.flowWithLifecycle(lifecycle).catch { }.collect { state ->
        state.loading { }
        state.success {
          listSubNote.run {
            this.clear()
            this.addAll(it)
          }
          setupAdapter(it)
        }
        state.error { }
      }
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
  }

  private fun setupInputSubNote() {
    binding.etNoteBody.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == 0) {
        subNoteViewModel.addSubNote(v.text.toString())
        v.text = ""
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
      setOnUpdateListener { item, text, isHasFocus ->
        if (isHasFocus) {
          idSubNote = item.idSubNote
          textSubNote = text
          return@setOnUpdateListener
        }

        subNoteViewModel.updateSubNote(text, item.idSubNote)
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
    binding.etNoteTitle.setText(if (isUpdateNote) noteModel?.activity else "")
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


