package com.example.myapplication.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myapplication.R

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

        val noteId = intent.getLongExtra(NOTIFICATION_KEY_NOTE_ID, -1)
        val noteText = intent.getStringExtra(NOTIFICATION_KEY_NOTE_TEXT)
        val noteUser = intent.getStringExtra(NOTIFICATION_KEY_NOTE_USER)

        val mBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.hi) + ", $noteUser")
                .setContentText(context.getString(R.string.remind_you) + " $noteText")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(makeDeleteAction(context, noteId))
                .addAction(makePostponeAction(context, noteId))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(Color.MAGENTA)
                .setAutoCancel(true)

        mNotificationManager.notify(0, mBuilder.build())
    }

    private fun makeDeleteAction(context: Context, noteId: Long): NotificationCompat.Action {
        val deleteIntent = Intent(context.applicationContext, NotificationActionService::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val deletePendingIntent = PendingIntent.getService(
            context.applicationContext,
            REQUEST_CODE_DELETE,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action.Builder(
            R.drawable.ic_delete,
            context.getString(R.string.delete),
            deletePendingIntent
        ).build()
    }

    private fun makePostponeAction(context: Context, noteId: Long): NotificationCompat.Action {
        val postponeIntent =
            Intent(context.applicationContext, NotificationActionService::class.java)
        postponeIntent.action = ACTION_POSTPONE
        postponeIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val postponePendingIntent = PendingIntent.getService(
            context.applicationContext,
            REQUEST_CODE_POSTPONE,
            postponeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action.Builder(
            R.drawable.ic_eye,
            context.getString(R.string.postpone),
            postponePendingIntent
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(mNotificationManager: NotificationManager) {
        if (mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
            val audioAttributes =
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                audioAttributes
            )
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL = "MY_NOTES_NOTIFICATION_CHANNEL"
        const val ACTION_DELETE = "MY_NOTES_NOTIFICATION_DELETE"
        const val ACTION_POSTPONE = "MY_NOTES_NOTIFICATION_POSTPONE"
        const val REQUEST_CODE_DELETE = 13423434
        const val REQUEST_CODE_POSTPONE = 423435554
        const val ACTION = "MY_NOTES_NOTIFICATION"
        const val NOTIFICATION_KEY_NOTE_TEXT = "MY_NOTES_NOTIFICATION_TEXT"
        const val NOTIFICATION_KEY_NOTE_USER = "MY_NOTES_NOTIFICATION_USER"
        const val NOTIFICATION_KEY_NOTE_ID = "MY_NOTES_NOTIFICATION_KEY_NOTE_ID"
    }
}