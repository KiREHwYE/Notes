package com.example.notes.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.model.NoteItem
import com.example.notes.NoteItemAdapter
import com.example.notes.viewModels.NoteItemsViewModel
import com.example.notes.views.NoteListFragmentDirections
import com.example.notes.databinding.NotesListFragmentBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*


class NoteListFragment : Fragment() {

    private var _binding : NotesListFragmentBinding? = null
    private val binding
    get() = checkNotNull(_binding){
        "Binding is null."
    }

    private val noteItemListViewModel : NoteItemsViewModel by viewModels()

    private val adapter : NoteItemAdapter by lazy { NoteItemAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotesListFragmentBinding.inflate(inflater, container, false)
        binding.noteList.layoutManager = LinearLayoutManager(context)

        binding.noteList.itemAnimator = null

        adapter.context = this.context
        binding.noteList.adapter = adapter

        binding.addButton.setOnClickListener {
            addNewNote()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                noteItemListViewModel.notes.collect { notes ->
//                    adapter.setData(notes)
                    adapter.differ.submitList(notes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ABRA", "DESTROY")
        _binding = null
    }

    private fun addNewNote(){
        try {
            findNavController()
                .navigate(
                    NoteListFragmentDirections.showNoteEditDialog()
                )
        } catch (exception: java.lang.Exception){}
    }
}