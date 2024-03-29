package com.obidia.testagrii.presentation.inputdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.obidia.testagrii.BuildConfig
import com.obidia.testagrii.databinding.FragmentInputDataBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.domain.model.SubNoteModel
import com.obidia.testagrii.event.DismissInputEvent
import com.obidia.testagrii.presentation.share.ShareViewModel
import com.obidia.testagrii.utils.error
import com.obidia.testagrii.utils.loading
import com.obidia.testagrii.utils.replaceIfNull
import com.obidia.testagrii.utils.success
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InputDataFragment : Fragment() {

  private val subNoteViewModel: SubNoteViewModel by viewModels()
  private val shareViewModel: ShareViewModel by activityViewModels()
  private var _binding: FragmentInputDataBinding? = null
  private val binding get() = _binding!!
  private var subNoteAdapter: ListSubNotesAdapter = ListSubNotesAdapter()

  @Inject
  lateinit var model: InputDataModel

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
    subNoteViewModel.setIsUpdate(model.isUpdateNote, model.noteModel?.id.replaceIfNull())
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
          model.idNote = it.replaceIfNull()
        }
    }
  }

  private fun getNoteId(): Int {
    return if (model.isUpdateNote) model.noteModel?.id.replaceIfNull()
    else model.idNote
  }

  private fun getListNoteObserver() {
    subNoteViewModel.getAllSubNote()
    lifecycleScope.launch {
      subNoteViewModel.listSUbNote.flowWithLifecycle(lifecycle).catch { }.collect { state ->
        state.loading { }
        state.success {
          model.listSubNote.run {
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
    model.isUpdateNote = arguments?.getBoolean(ARGS_IS_UPDATE_NOTE).replaceIfNull()
    model.noteModel = arguments?.getParcelable(ARGS_DATA_NOTE)
  }

  private fun setupView() {
    setupAnimation()
    registerBackButton()
    setupRecycleView()
    adjustViewInput()
    setupInputSubNote()
  }


  private fun setupAnimation() {
    sharedElementEnterTransition =
      TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    binding.container.transitionName = BuildConfig.APPLICATION_ID
  }

  private fun registerBackButton() {
    activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() = back()
    })
  }

  private fun back() {
    model.title = binding.etNoteTitle.text.toString()
    setText(model.dataSubNoteModel)
    model.listUpdate.forEach {
      subNoteViewModel.updateSubNote(it)
    }

    shareViewModel.saveShare(
      DismissInputEvent(
        model.listSubNote, getNoteId(), model.title
      )
    )
    findNavController().popBackStack()
  }

  private fun setupInputSubNote() {
//    binding.etNoteBody.setOnEditorActionListener { v, actionId, _ ->
//      if (actionId == 0) {
//        subNoteViewModel.addSubNote(v.text.toString())
//        v.text = ""
//      }
//      false
//    }
  }

  private fun setupAdapter(list: ArrayList<SubNoteModel>) {
    val listSubNote = ListItemAdapter.transform(list)
    subNoteAdapter.run {
      submitList(listSubNote)
      setOnDeleteListener {
        it?.let { it1 -> subNoteViewModel.deleteSubNote(it1) }
      }
      setOnUpdateListener { item, isHasFocus ->
        if (isHasFocus) {
          model.dataSubNoteModel = item
          return@setOnUpdateListener
        }

        setText(item)
      }
      setOnCheckBoxListener { item, position ->
        subNoteAdapter.updateItem(position)
        val listFilter = model.listUpdate.filter {
          it.idSubNote == item?.idSubNote
        }

        if (listFilter.isEmpty()) {
          item?.let { model.listUpdate.add(it) }
          return@setOnCheckBoxListener
        }

        val index = model.listUpdate.indexOfFirst {
          it.idSubNote == item?.idSubNote
        }

        model.listUpdate[index].apply {
          this.isFinished = item?.isFinished.replaceIfNull()
        }
      }
      setOnAddItemListener {
        setText(model.dataSubNoteModel)
        model.listUpdate.forEach {
          subNoteViewModel.updateSubNote(it)
        }
        model.listUpdate.clear()
        subNoteViewModel.addSubNote()
      }
    }
  }

  private fun setText(item: SubNoteModel?) {

    val listFilter = model.listUpdate.filter {
      it.idSubNote == item?.idSubNote
    }

    if (listFilter.isEmpty()) {
      item?.let { model.listUpdate.add(it) }
      return
    }

    val index = model.listUpdate.indexOfFirst {
      it.idSubNote == item?.idSubNote
    }

    model.listUpdate[index].apply {
      this.text = item?.text.replaceIfNull()
    }
  }

//  override fun onStop() {
//    super.onStop()
//    model.title = binding.etNoteTitle.text.toString()
//    setText(model.dataSubNoteModel)
//    model.listUpdate.forEach {
//      subNoteViewModel.updateSubNote(it)
//    }
////    onDisMissListener?.invoke(model.listSubNote, getNoteId(), model.title)
//    shareViewModel.saveShare(DismissInputEvent(
//      model.listSubNote, getNoteId(), model.title
//    ))
//  }

  private fun setupRecycleView() {
    binding.rvSubNote.run {
      adapter = subNoteAdapter
      layoutManager = LinearLayoutManager(requireContext())
    }
  }

  private fun adjustViewInput() {
    binding.etNoteTitle.setText(if (model.isUpdateNote) model.noteModel?.activity else "")
  }

  companion object {
    private const val ARGS_DATA_NOTE = BuildConfig.APPLICATION_ID + "ARGS_DATA_NOTE"
    private const val ARGS_IS_UPDATE_NOTE = BuildConfig.APPLICATION_ID + "ARGS_IS_UPDATE_NOTE"

    fun newInstance(
      data: NoteModel? = null,
      isUpdateNote: Boolean = false
    ): Bundle {
      val dialog = InputDataFragment()
      val bundle = Bundle()
      bundle.putParcelable(ARGS_DATA_NOTE, data)
      bundle.putBoolean(ARGS_IS_UPDATE_NOTE, isUpdateNote)
      dialog.arguments = bundle
      return bundle
    }
  }
}


