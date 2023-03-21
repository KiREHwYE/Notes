package com.kire.notes

import android.app.Application
import com.kire.notes.viewModels.NoteRepository

class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}