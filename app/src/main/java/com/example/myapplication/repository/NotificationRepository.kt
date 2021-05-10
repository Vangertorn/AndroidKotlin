package com.example.myapplication.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
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
        intent.action = NotificationReceiver.ACTION
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, note.id)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_TEXT, note.title)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_USER, note.userName)
        return PendingIntent.getBroadcast(context, 0, intent, FLAG_UPDATE_CURRENT)
    }

    fun postponeNoteTimeByFiveMinutes(note: Note): Note{
        val time = dateFormatter.parse(note.date!!)
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.add(Calendar.MINUTE, 5)
        return note.copy(date = dateFormatter.format(calendar.time))
    }

    companion object {


    }

}