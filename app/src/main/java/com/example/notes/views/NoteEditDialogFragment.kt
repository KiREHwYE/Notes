package com.example.notes.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.notes.views.NoteEditDialogFragmentArgs
import com.example.notes.viewModels.NoteItemsViewModel
import com.example.notes.databinding.NoteEditDialogFragmentBinding
import com.example.notes.model.NoteItem
import kotlinx.coroutines.launch
import java.util.*

class NoteEditDialogFragment : DialogFragment() {

    private var _binding : NoteEditDialogFragmentBinding ?= null
    private val binding
        get() = checkNotNull(_binding){
            "Binding is null"
        }

    private val noteItemsViewModel: NoteItemsViewModel by viewModels()
    private val args: NoteEditDialogFragmentArgs by navArgs()

    private var _note : NoteItem = NoteItem(
        UUID.randomUUID(),
        noteTitle = "",
        noteText = ""
    )
    private val note
    get() = checkNotNull(_note){
        "Note is not created"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteEditDialogFragmentBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                args.noteId?.let { noteId ->
                    titleText.setText(noteItemsViewModel.getNote(noteId).noteTitle)
                    mainText.setText(noteItemsViewModel.getNote(noteId).noteText)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            titleText.doOnTextChanged { text, start, before, count ->
                viewLifecycleOwner.lifecycleScope.launch {

                    if (args.noteId == null){
                        _note = note.copy(noteTitle = text.toString())
                    }
                    else {
                        if (noteItemsViewModel.getNote(args.noteId!!).noteTitle != text.toString()) {
                            noteItemsViewModel.updateNote(
                                noteItemsViewModel.getNote(args.noteId!!)
                                    .copy(noteTitle = text.toString())
                            )
                        }
                    }
                }
            }

            mainText.doOnTextChanged { text, start, before, count ->
                viewLifecycleOwner.lifecycleScope.launch {

                    if (args.noteId == null){
                        _note = note.copy(noteTitle = text.toString())
                    }
                    else {
                        if (noteItemsViewModel.getNote(args.noteId!!).noteText != text.toString()) {
                            noteItemsViewModel.updateNote(
                                noteItemsViewModel.getNote(args.noteId!!)
                                    .copy(noteText = text.toString())
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (note.noteTitle != "" || note.noteText != ""){
            viewLifecycleOwner.lifecycleScope.launch {
                noteItemsViewModel.addNote(note)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}