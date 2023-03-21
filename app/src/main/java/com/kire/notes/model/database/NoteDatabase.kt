package com.kire.notes.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kire.notes.model.NoteItem

@Database(entities = [NoteItem::class], version = 2, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}