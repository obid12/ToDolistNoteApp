package com.obidia.testagrii.presentation.listnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.obidia.testagrii.BuildConfig
import com.obidia.testagrii.R
import com.obidia.testagrii.databinding.FragmentListNoteBinding
import com.obidia.testagrii.databinding.ItemNoteBinding
import com.obidia.testagrii.domain.model.NoteAndSubNoteModel
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.inputdata.InputDataFragment
import com.obidia.testagrii.presentation.share.ShareViewModel
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
  private var lisDeleteNote: MutableList<NoteModel> = mutableListOf()
  private val shareViewModel: ShareViewModel by activityViewModels()

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

  override fun onResume() {
    setupDismissInput()
    super.onResume()
  }

  private fun setupDismissInput() {
    shareViewModel.share.observe(viewLifecycleOwner) {
      if (it.title.isEmpty() && it.listSubNoteModel.isEmpty()) {
        noteViewModel.deleteNoteById(it.idNote)
        return@observe
      }

      noteViewModel.updateNote(it.title, it.idNote)
    }
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
    isUpdateNote: Boolean = false,
    binding: ItemNoteBinding? = null
  ) {
    val extras = binding?.container?.let {
      it to BuildConfig.APPLICATION_ID
    }?.let {
      FragmentNavigatorExtras(
        it
      )
    }

    binding?.container?.transitionName = BuildConfig.APPLICATION_ID

    val bundle = InputDataFragment.newInstance(
      data,
      isUpdateNote
    )
    findNavController().navigate(
      resId = R.id.action_listNoteFragment_to_inputDataFragment,
      args = bundle,
      navOptions = null,
      extras
    )
  }

  private fun setupAdapter(list: ArrayList<NoteAndSubNoteModel>) {
    itemAdapter.run {
      submitList(list)
      setOnClickItem { data, binding ->
        gotoInputDialog(data.noteEntity, true, binding)
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
      adapter = itemAdapter
      layoutManager = StaggeredGridLayoutManager(
        2,
        StaggeredGridLayoutManager.VERTICAL
      )
    }
  }
}