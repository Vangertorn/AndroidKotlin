package com.example.myapplication.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myapplication.models.Note
import com.example.myapplication.notification.NotificationReceiver
import java.util.*

class NotificationRepository(private val context: Context, private val alarmManager: AlarmManager) {

    private val dateFormatter = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun setNotification(note: Note) {
        val alarmTimeAtUTC = dateFormatter.parse(note.date!!)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC!!.time,
            makeIntent(note)
        )
    }

    fun unsetNotification(note: Note) {
        alarmManager.cancel(makeIntent(note))
    }

    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = ACTION
        intent.putExtra(NOTIFICATION_KEY_NOTE_ID, note.id)
        intent.putExtra(NOTIFICATION_KEY_NOTE_TEXT, note.title)
        intent.putExtra(NOTIFICATION_KEY_NOTE_USER, note.userName)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    fun postponeNoteTimeByFiveMinutes(note: Note): Note{
        val time = dateFormatter.parse(note.date!!)
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.add(Calendar.MINUTE, 5)
        return note.copy(date = dateFormatter.format(calendar.time))
    }

    companion object {
        const val ACTION = "MY_NOTES_NOTIFICATION"
        const val NOTIFICATION_KEY_NOTE_TEXT = "MY_NOTES_NOTIFICATION_TEXT"
        const val NOTIFICATION_KEY_NOTE_USER = "MY_NOTES_NOTIFICATION_USER"
        const val NOTIFICATION_KEY_NOTE_ID = "MY_NOTES_NOTIFICATION_KEY_NOTE_ID"
        const val ACTION_DELETE = "MY_NOTES_NOTIFICATION_DELETE"
        const val ACTION_POSTPONE = "MY_NOTES_NOTIFICATION_POSTPONE"
    }

}