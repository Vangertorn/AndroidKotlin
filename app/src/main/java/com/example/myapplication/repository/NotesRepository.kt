package com.example.myapplication.repository

import com.example.myapplication.database.dao.NotesDao
import com.example.myapplication.database.dao.UsersDao
import com.example.myapplication.datastore.AppSettings
import com.example.myapplication.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class NotesRepository(
    private val notesDao: NotesDao,
    private val appSettings: AppSettings,
    private val usersDao: UsersDao,
    private val notificationRepository: NotificationRepository
) {

    @ExperimentalCoroutinesApi
    val currentUserNotesFlow: Flow<List<Note>> =
        appSettings.userNameFlow()
            .flatMapLatest { userName ->
                usersDao.getUserInfoFlow(userName).map { it?.notes ?: emptyList() }
            }

    suspend fun getCurrentUserNote(): List<Note> {
        return usersDao.getUserInfo(appSettings.userName())?.notes ?: emptyList()
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }

    suspend fun updateNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            val oldNotes = getCurrentUserNote()
            val result = (oldNotes + notes).distinctBy { it.date + it.title }
            notesDao.updateTableNotes(result)
        }
    }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            if (note.date !== null) {
                notificationRepository.setNotification(note)
            }
            notesDao.insertNote(
                Note(
                    title = note.title,
                    date = note.date,
                    userName = appSettings.userName()
                )
            )
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            val oldNote = notesDao.getNoteById(note.id)
            if (oldNote.date !== null) {
                notificationRepository.unsetNotification(oldNote)
            }

            notesDao.updateNote(note)
            if (note.date !== null) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            if (note.date !== null) {
                notificationRepository.unsetNotification(note)
            }
            notesDao.deleteNote(note)
        }
    }
}