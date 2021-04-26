package com.example.myapplication.screen.note_details

import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.models.Note
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NoteDetailsViewModel(private val notesRepository: NotesRepository) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch { notesRepository.saveNote(note) }
    }

    fun updateNote(note: Note) {
        launch { notesRepository.updateNote(note) }
    }

}