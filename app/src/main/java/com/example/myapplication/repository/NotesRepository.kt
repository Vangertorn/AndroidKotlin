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
            notesDao.updateTableNotes(usersDao.getUserInfo(appSettings.userName())!!.notes.map {
                if (note.position == 0) {
                    Note(
                        id = it.id,
                        alarmEnabled = it.alarmEnabled,
                        cloud = it.cloud,
                        title = it.title,
                        date = it.date,
                        userName = it.userName,
                        postscript = it.postscript,
                        position = it.position + 1
                    )
                } else if (note.position > it.position) {
                    Note(
                        id = it.id,
                        alarmEnabled = it.alarmEnabled,
                        cloud = it.cloud,
                        title = it.title,
                        date = it.date,
                        userName = it.userName,
                        postscript = it.postscript,
                        position = it.position
                    )
                } else {
                    Note(
                        id = it.id,
                        alarmEnabled = it.alarmEnabled,
                        cloud = it.cloud,
                        title = it.title,
                        date = it.date,
                        userName = it.userName,
                        postscript = it.postscript,
                        position = it.position + 1
                    )
                }
            })

            if (note.id == 0L) {
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
            } else {
                reSaveNote(note)
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
            notesDao.updateTableNotes(usersDao.getUserInfo(appSettings.userName())!!.notes.map {
                if (it.position > note.position) {
                    Note(
                        id = it.id,
                        alarmEnabled = it.alarmEnabled,
                        cloud = it.cloud,
                        title = it.title,
                        date = it.date,
                        userName = it.userName,
                        postscript = it.postscript,
                        position = it.position - 1
                    )
                } else {
                    Note(
                        id = it.id,
                        alarmEnabled = it.alarmEnabled,
                        cloud = it.cloud,
                        title = it.title,
                        date = it.date,
                        userName = it.userName,
                        postscript = it.postscript,
                        position = it.position
                    )
                }

            })
        }
    }

    suspend fun updateNoteById(noteId: Long, postscriptText: String) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let {
                notesDao.updateNote(
                    Note(
                        id = it.id,
                        title = it.title,
                        postscript = postscriptText,
                        date = it.date,
                        userName = it.userName,
                        cloud = it.cloud,
                        alarmEnabled = false
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

    private suspend fun reSaveNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(
                Note(
                    id = note.id,
                    title = note.title,
                    date = note.date,
                    userName = appSettings.userName(),
                    alarmEnabled = note.alarmEnabled,
                    cloud = note.cloud,
                    position = note.position,
                    postscript = note.postscript
                )
            )
        }
    }
}