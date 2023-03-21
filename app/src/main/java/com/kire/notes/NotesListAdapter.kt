package com.kire.notes

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kire.notes.databinding.NoteItemBinding
import com.kire.notes.views.NoteListFragmentDirections
import com.kire.notes.model.NoteItem
import com.kire.notes.viewModels.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteItemAdapter(): RecyclerView.Adapter<NoteItemAdapter.NoteItemViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    var context : Context ?= null

    private val noteRepository = NoteRepository.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NoteItemViewHolder(noteItemBinding: NoteItemBinding): RecyclerView.ViewHolder(noteItemBinding.root) {

        private val binding = noteItemBinding
        fun bind(noteItem: NoteItem) {

            binding.apply {
                title.text = noteItem.noteTitle

                if (noteItem.noteText.count() < 500)
                    note.text = noteItem.noteText
                else note.text = noteItem.noteText.substring(0, 500) + "..."

                root.setOnClickListener { view ->
                    view.findNavController()
                        .navigate(NoteListFragmentDirections
                            .showNoteEditDialog(noteItem.id)
                        )
                }

                root.setOnLongClickListener { view ->
                    val builder = AlertDialog.Builder(context)
                    val customView = LayoutInflater.from(context).inflate(R.layout.note_delete_asking_fragment, null)
                    builder.setView(customView)

                    val yesButton = customView.findViewById<Button>(R.id.yes_button)
                    val noButton = customView.findViewById<Button>(R.id.no_button)

                    val dialog = builder.create()
                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    yesButton.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            noteRepository.deleteNote(noteItem.id)
                            dialog.dismiss()
                        }
                    }

                    noButton.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()

                    return@setOnLongClickListener true
                }
            }
        }
    }
}