package com.obidia.testagrii.presentation.listnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.obidia.testagrii.databinding.FragmentListNoteBinding
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.inputdata.InputDataFragment
import com.obidia.testagrii.utils.error
import com.obidia.testagrii.utils.loading
import com.obidia.testagrii.utils.success
import com.obidia.testagrii.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListNoteFragment : Fragment() {

  private lateinit var binding: FragmentListNoteBinding
  private val noteViewModel: NoteViewModel by viewModels()
  private val itemAdapter: ListAdapter = ListAdapter()
  private var isBottomSheetShowing = false
  private var lisDeleteNote: MutableList<NoteModel> = mutableListOf()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentListNoteBinding.inflate(inflater, container, false)
    setupView()
    return binding.root
  }

  private fun setupView() {
    setupRecycleView()
    setupObserver()
    setupFloatBtn()
  }

  private fun setupObserver() {
    lifecycleScope.launch {
      noteViewModel.getAllNote().flowWithLifecycle(lifecycle).catch { }.collect { state ->
        state.loading { }
        state.success {
          setupAdapter(it)
        }
        state.error { }
      }
    }
  }

  private fun setupFloatBtn() {
    binding.floatBtn.setOnClickListener {
      noteViewModel.addNote(NoteModel(0, "", "", "", false))
      gotoInputDialog()
    }
  }

  private fun gotoInputDialog(
    data: NoteModel? = null,
    isUpdateNote: Boolean = false
  ) {
    val dialogFragment = InputDataFragment.newInstance(
      data,
      isUpdateNote
    ).also {
      it.setOnDisMissListener { list, id, title ->
        isBottomSheetShowing = false

        if (title.isEmpty() && list.isEmpty()) {
          noteViewModel.deleteNoteById(id)
          return@setOnDisMissListener
        }

        noteViewModel.updateNote(title, id)
      }
    }

    val fragmentManager = childFragmentManager
    dialogFragment.show(fragmentManager, dialogFragment::class.java.simpleName)
  }

  private fun setupAdapter(list: ArrayList<NoteAndSubNoteModel>) {
    itemAdapter.run {
      submitList(list)
      setOnClickItem {
        if (isBottomSheetShowing) return@setOnClickItem

        isBottomSheetShowing = true
        gotoInputDialog(it.noteEntity, true)
      }

      setOnCheckItem { data, isChecked ->
        if (!isChecked) {
          lisDeleteNote.remove(data.noteEntity)
          setIvDelete()
          return@setOnCheckItem
        }

        lisDeleteNote.add(data.noteEntity)
        setIvDelete()
      }
    }
  }

  private fun setIvDelete() {
    binding.ivDelete.run {
      visible(lisDeleteNote.isNotEmpty())
      setOnClickListener {
        val listDeleteRoom: MutableList<NoteModel> = arrayListOf()
        listDeleteRoom.addAll(lisDeleteNote)
        noteViewModel.deleteNote(listDeleteRoom)
        lisDeleteNote.clear()
        this.visible(false)
      }
    }
  }

  private fun setupRecycleView() {
    binding.rv.run {
      itemAnimator = null
      adapter = itemAdapter
      layoutManager = StaggeredGridLayoutManager(
        2,
        StaggeredGridLayoutManager.VERTICAL
      )
    }
  }
}