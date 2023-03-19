package com.example.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.NoteItemBinding
import com.example.notes.views.NoteListFragmentDirections
import com.example.notes.model.NoteItem
import java.util.UUID


class NoteItemAdapter(): RecyclerView.Adapter<NoteItemAdapter.NoteItemViewHolder>() {

//    private var noteItemList: List<NoteItem> = emptyList()
//    set(newList){
////        field = newList
////        notifyDataSetChanged()
////        val diffCallBack = NotesDiffCallBack(field, newList)
////        val diffResult = DiffUtil.calculateDiff(diffCallBack)
////        field = newList
////        diffResult.dispatchUpdatesTo(this)
//    }
 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
//        holder.bind(noteItemList[position])

        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }
    override fun getItemCount(): Int {
//        return noteItemList.size
        return differ.currentList.size
    }
//    fun setData(noteList : List<NoteItem>){
//        noteItemList = noteList
//    }

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

    private val differCallBack = object : DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}