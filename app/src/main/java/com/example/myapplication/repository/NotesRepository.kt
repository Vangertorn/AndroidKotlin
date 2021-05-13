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
    val currentUserNotesFlow: Flow<MutableList<Note>> =
        appSettings.userNameFlow()
            .flatMapLatest { userName ->
                usersDao.getUserInfoFlow(userName).map { it?.notes ?: mutableListOf() }
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
            notes.forEach {
                if (it.alarmEnabled) {
                    notificationRepository.setNotification(it)
                }
            }
        }
    }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            val id = notesDao.insertNote(
                Note(
                    title = note.title,
                    date = note.date,
                    userName = appSettings.userName(),
                    alarmEnabled = note.alarmEnabled
                )
            )
            if (note.alarmEnabled) {
                notificationRepository.setNotification(notesDao.getNoteById(id)!!)
            }
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(note.id)?.let { oldNote ->
                notificationRepository.unsetNotification(oldNote)
            }
            notesDao.updateNote(note)
            if (note.alarmEnabled) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            if (note.alarmEnabled) {
                notificationRepository.unsetNotification(note)
            }
            notesDao.deleteNote(note)
        }
    }

    suspend fun  updateNoteById(noteId: Long, newText: String){
        withContext(Dispatchers.IO){
            notesDao.getNoteById(noteId)?.let {
               notesDao.updateNote(
                   Note(
                       id = it.id,
                       title = newText,
                       date = it.date,
                       userName = it.userName,
                       cloud = it.cloud,
                       alarmEnabled = it.alarmEnabled
                   )
               )
            }
        }
    }

    suspend fun deleteNoteByID(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let {
                notificationRepository.unsetNotification(it)
                notesDao.deleteNote(it)
            }
        }
    }

    suspend fun postponeNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let {
                notificationRepository.unsetNotification(it)
                val postponeNote = notificationRepository.postponeNoteTimeByFiveMinutes(it)
                notesDao.updateNote(postponeNote)
                notificationRepository.setNotification(postponeNote)

            }
        }
    }
}