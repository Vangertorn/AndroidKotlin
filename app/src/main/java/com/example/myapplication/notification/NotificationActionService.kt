package com.example.myapplication.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.myapplication.repository.NotesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class NotificationActionService : Service(), KoinComponent {

    private val notesRepository: NotesRepository by inject()
    private var noteId: Long = -1

    //    private var textNote: String? = ""
    private var replyText: CharSequence? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            val result = androidx.core.app.RemoteInput.getResultsFromIntent(it)
            if (result != null) {
                replyText = result.getCharSequence(NotificationReceiver.KEY_TEXT_REPLAY)
            }
            noteId = it.getLongExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, -1)
//            textNote = it.getStringExtra(NotificationReceiver.KEY_TEXT_REPLAY)

            when (it.action) {
                NotificationReceiver.ACTION_DELETE -> {
                    GlobalScope.launch {
                        notesRepository.deleteNoteByID(noteId)
                    }

                }
                NotificationReceiver.ACTION_POSTPONE -> {
                    GlobalScope.launch {
                        notesRepository.postponeNoteById(noteId)
                    }
                }
                NotificationReceiver.ACTION_EDIT_NOTE -> {
                    GlobalScope.launch {
                        notesRepository.updateNoteById(noteId, replyText.toString())
                    }

                }

                else -> Unit
            }
        }
        stopSelf()
        return START_STICKY
    }
}