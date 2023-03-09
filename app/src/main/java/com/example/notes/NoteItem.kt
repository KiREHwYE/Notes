package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "note")
data class NoteItem(
    @PrimaryKey val id: UUID,
    val noteTitle: String,
    val noteText: String
)
