package com.example.myapplication.repository

import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext


class NotesRepository(private val notesDao: NotesDao, private val appSettings: AppSettings) {

    @ExperimentalCoroutinesApi
    val currentUserNotesFlow: Flow<List<Note>> =
        appSettings.userIdFlow()
            .flatMapLatest { userId ->
                notesDao.getALLNotesFlowByUserId(userId)
            }

    suspend fun getCurrentUserNote(): List<Note> {
        return notesDao.getAllNotesByUserId(appSettings.userId())
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }

    suspend fun insertNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            val oldNotes = getCurrentUserNote()
            if (oldNotes.isEmpty()) notesDao.insertNotes(notes) else {
                for (note in notes) {
                    var count = true
                    for (oldNote in oldNotes) {
                        if (oldNote.title == note.title && oldNote.date == note.date) {
                            count = false
                        }
                    }
                    if (count) notesDao.insertNote(note)
                }
            }
        }
    }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(
                Note(
                    title = note.title,
                    date = note.date,
                    userId = appSettings.userId()
                )
            )
        }
    }


    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }
}