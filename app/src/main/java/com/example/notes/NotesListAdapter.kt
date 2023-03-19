package com.example.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.NoteItemBinding
import com.example.notes.model.NoteItem
import java.util.UUID


class NoteItemAdapter(

    ): RecyclerView.Adapter<NoteItemAdapter.NoteItemViewHolder>() {

    private var noteItemList: List<NoteItem> = emptyList()
    set(newList){
        field = newList
        notifyDataSetChanged()
    }
 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val noteItem = noteItemList[position]
        holder.bind(noteItem)


    }
    override fun getItemCount(): Int {
        return noteItemList.size
    }
    fun setData(noteList : List<NoteItem>){
        this.noteItemList = noteList
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
                    view.findNavController()
                        .navigate(NoteListFragmentDirections
                            .showNoteDeleteDialog(noteItem.id)
                        )
                    return@setOnLongClickListener true
                }
            }
        }
    }
}