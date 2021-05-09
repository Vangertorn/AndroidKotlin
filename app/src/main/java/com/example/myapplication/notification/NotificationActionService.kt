package com.example.myapplication.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.repository.NotificationRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationActionService : Service(), KoinComponent {
    private val notesRepository: NotesRepository by inject()
    private var noteId: Long = -1
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            noteId = it.getLongExtra(NotificationRepository.NOTIFICATION_KEY_NOTE_ID, -1)
            when (it.action) {
                NotificationRepository.ACTION_DELETE -> {
                    GlobalScope.launch {
                        notesRepository.deleteNoteByID(noteId)
                    }
                }
                NotificationRepository.ACTION_POSTPONE -> {
                    GlobalScope.launch {
                        notesRepository.postponeNoteById(noteId)
                    }
                }
                else -> Unit
            }
            stopSelf()
        }
        return START_STICKY
    }
}