package com.example.myapplication.screen.note_details

import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NoteDetailsViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch { notesDao.insertNote(note) }
    }

    fun updateNote(note: Note) {
        launch { notesDao.updateNote(note) }
    }

}