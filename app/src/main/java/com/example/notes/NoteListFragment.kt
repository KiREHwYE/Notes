package com.example.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.NotesListFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class NoteListFragment : Fragment() {

    private var _binding : NotesListFragmentBinding? = null
    private val binding
    get() = checkNotNull(_binding){
        "Binding is null."
    }

    private val noteItemListViewModel : NoteItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotesListFragmentBinding.inflate(inflater, container, false)
        binding.noteList.setHasFixedSize(true)
        binding.noteList.layoutManager = LinearLayoutManager(context)

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
                    binding.noteList.adapter =
                        NoteItemAdapter(notes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun addNewNote(){

        val newNote = NoteItem(
            id= UUID.randomUUID(),
            noteTitle = "",
            noteText = ""
        )

        viewLifecycleOwner.lifecycleScope.launch{
            noteItemListViewModel.addNote(newNote)
        }

        try {
            findNavController()
                .navigate(
                    NoteListFragmentDirections
                        .showNoteEditDialog(newNote.id)
                )
        } catch (exception: java.lang.Exception){}
    }
}