package com.example.notes.database

import androidx.room.*
import com.example.notes.NoteItem
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM note WHERE id = (:id)")
    suspend fun getNote(id: UUID): NoteItem

    @Insert
    suspend fun addNote(note: NoteItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Delete
    suspend fun deleteNote(note: NoteItem)
}