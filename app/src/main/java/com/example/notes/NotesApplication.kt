package com.example.notes

import android.app.Application
import com.example.notes.viewModels.NoteRepository

class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}