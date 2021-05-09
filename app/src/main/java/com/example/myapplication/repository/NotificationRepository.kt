package com.example.myapplication.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myapplication.models.Note
import com.example.myapplication.notification.NotificationReceiver
import java.util.*

class NotificationRepository(private val contex: Context, private val alarmManager: AlarmManager) {

    private val dateFormatter = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun setNotification(note: Note) {
        val alarmTimeAtUTC = dateFormatter.parse(note.date!!)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC!!.time,
            makeIntent(note)
        )
    }

    fun unsetNotification(note: Note) {
        alarmManager.cancel(makeIntent(note))
    }


    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(contex, NotificationReceiver::class.java)
        intent.action = INTENT_ACTION
        intent.putExtra(PLANNER_APP_NOTIFICATION_TEXT, note.title)
        intent.putExtra(PLANNER_APP_NOTIFICATION_USER, note.userName)
        return PendingIntent.getBroadcast(contex, 0, intent, 0)
    }

    companion object {
        const val INTENT_ACTION = "PLANNER_APP_NOTIFICATION"
        const val PLANNER_APP_NOTIFICATION_TEXT = "PLANNER_APP_NOTIFICATION_TEXT"
        const val PLANNER_APP_NOTIFICATION_USER = "PLANNER_APP_NOTIFICATION_USER"
    }
}