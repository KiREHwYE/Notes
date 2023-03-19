package com.example.notes.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.NoteDeleteDialogFragmentArgs
import com.example.notes.NoteDeleteDialogFragmentDirections
import com.example.notes.viewModels.NoteItemsViewModel
import com.example.notes.databinding.NoteDeleteAskingFragmentBinding
import kotlinx.coroutines.launch

class NoteDeleteDialogFragment : DialogFragment() {

    private var _binding : NoteDeleteAskingFragmentBinding? = null
    private val binding
    get() = checkNotNull(_binding){
        "Binding is null"
    }

    private val noteItemListViewModel: NoteItemsViewModel by viewModels()

    private val args: NoteDeleteDialogFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteDeleteAskingFragmentBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            yesButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch{
                    noteItemListViewModel.deleteNote(args.noteId)

                    findNavController()
                        .navigate(
                            NoteDeleteDialogFragmentDirections.showNoteListFragment()
                        )
                }
            }

            noButton.setOnClickListener {
                findNavController()
                    .navigate(
                        NoteDeleteDialogFragmentDirections.showNoteListFragment()
                    )
            }
        }

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}