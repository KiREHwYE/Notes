package com.kire.notes.viewModels

import android.content.Context
import androidx.room.Room
import com.kire.notes.model.database.NoteDatabase
import com.kire.notes.model.NoteItem
import kotlinx.coroutines.flow.Flow
import java.util.*


class NoteRepository private constructor(context: Context) {

    private val database: NoteDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "note-database"
        )
        .fallbackToDestructiveMigration()
        .build()

    fun getNotes() : Flow<List<NoteItem>> = database.noteDao().getNotes()
    suspend fun getNote(id: UUID) : NoteItem = database.noteDao().getNote(id)
    suspend fun addNote(note: NoteItem) = database.noteDao().addNote(note)
    suspend fun deleteNote(id: UUID) = database.noteDao().deleteNote(getNote(id))
    suspend fun updateNote(note: NoteItem) = database.noteDao().updateNote(note)


    companion object{
        private var INSTANCE : NoteRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null){
                INSTANCE = NoteRepository(context)
            }
        }

        fun get(): NoteRepository {
            return INSTANCE ?:
            throw java.lang.IllegalStateException("NoteRepository must be initialized")
        }
    }
}