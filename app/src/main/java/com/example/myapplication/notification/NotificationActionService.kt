package com.example.myapplication.notification

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
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

    private var replyText: CharSequence? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationBuilderManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent?.let {
            val result = androidx.core.app.RemoteInput.getResultsFromIntent(it)
            if (result != null) {
                replyText = result.getCharSequence(NotificationReceiver.KEY_TEXT_REPLAY)
            }
            noteId = it.getLongExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, -1)

            when (it.action) {
                NotificationReceiver.ACTION_DELETE -> {
                    GlobalScope.launch {
                        notesRepository.deleteNoteByID(noteId)
                        notificationBuilderManager.cancel("TAG", 0)
                    }

                }
                NotificationReceiver.ACTION_POSTPONE -> {
                    GlobalScope.launch {
                        notesRepository.postponeNoteById(noteId)
                        notificationBuilderManager.cancel("TAG", 0)
                    }
                }
                NotificationReceiver.ACTION_EDIT_NOTE -> {
                    GlobalScope.launch {
                        notesRepository.updateNoteById(noteId, replyText.toString())
                        notificationBuilderManager.deleteNotificationChannel(NotificationReceiver.NOTIFICATION_CHANNEL)
                    }


                }

                else -> Unit
            }
        }
        stopSelf()
        return START_STICKY
    }
}