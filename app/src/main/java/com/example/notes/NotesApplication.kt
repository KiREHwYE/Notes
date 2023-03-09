package com.example.notes

import android.app.Application

class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}