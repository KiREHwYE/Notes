package com.example.notes

import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class NoteItemsViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()

    private val _notes: MutableStateFlow<List<NoteItem>> = MutableStateFlow(emptyList())
    val notes: StateFlow<List<NoteItem>>
    get() = _notes.asStateFlow()

    init {
            viewModelScope.launch {
                noteRepository.getNotes().collect(){
                    _notes.value = it
                }
            }
    }

    suspend fun addNote(note: NoteItem){
        noteRepository.addNote(note)
    }

    suspend fun deleteNote(id: UUID){
        noteRepository.deleteNote(id)
    }

    suspend fun updateNote(note: NoteItem){
        noteRepository.updateNote(note)
    }

    suspend fun getNote(id: UUID): NoteItem{
        return noteRepository.getNote(id)
    }

}