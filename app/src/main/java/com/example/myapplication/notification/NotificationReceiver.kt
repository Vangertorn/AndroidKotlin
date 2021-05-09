package com.example.myapplication.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.repository.NotificationRepository

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, intent)
    }

    private fun showNotification(context: Context, intent: Intent) {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, NotificationReceiver::class.java), 0
        )
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(mNotificationManager)
        }

        val noteId = intent.getLongExtra(NotificationRepository.NOTIFICATION_KEY_NOTE_ID, -1)
        val noteText = intent.getStringExtra(NotificationRepository.NOTIFICATION_KEY_NOTE_TEXT)
        val noteUser = intent.getStringExtra(NotificationRepository.NOTIFICATION_KEY_NOTE_USER)

        val mBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.hi) + ", $noteUser")
                .setContentText(context.getString(R.string.remind_you) + " $noteText")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .addAction()
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
        mNotificationManager.notify(0, mBuilder.build())
    }
//    private fun makeDeleteAction(context: Context, noteId: Long): NotificationCompat.Action{
//        val deleteIntent = Intent(context.applicationContext, NotificationActionService::class.java)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(mNotificationManager: NotificationManager) {
        if (mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL = "MY_NOTES_NOTIFICATION_CHANNEL"
    }
}