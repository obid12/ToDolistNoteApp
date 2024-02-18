package com.obidia.testagrii.presentation.listnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.obidia.testagrii.databinding.FragmentListNoteBinding
import com.obidia.testagrii.domain.model.NoteModel
import com.obidia.testagrii.presentation.inputdata.InputDataFragment
import com.obidia.testagrii.presentation.update.UpdateDataFragment
import com.obidia.testagrii.utils.error
import com.obidia.testagrii.utils.loading
import com.obidia.testagrii.utils.success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListNoteFragment : Fragment() {

  private lateinit var binding: FragmentListNoteBinding
  private val noteViewModel: NoteViewModel by viewModels()
  private val itemAdapter: ListAdapter = ListAdapter()

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
      noteViewModel.getAllNote().flowWithLifecycle(lifecycle).catch {  }.collect { state ->
        state.loading {  }
        state.success {
          Log.d("KESINI", "$it")
          setupAdapter(it)
        }
        state.error {  }
      }
    }
  }

  private fun setupFloatBtn() {
    binding.floatBtn.setOnClickListener {
      val dialogFragment = InputDataFragment()
      val fragmentManager = childFragmentManager
      dialogFragment.show(fragmentManager, dialogFragment::class.java.simpleName)
    }
  }

  private fun setupAdapter(list: ArrayList<NoteModel>) {
    itemAdapter.run {
      submitList(list)
      setOnClickItem {
        val dialogFragment = UpdateDataFragment(it)
        val fragmentManager = childFragmentManager
        dialogFragment.show(fragmentManager, dialogFragment::class.java.simpleName)
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