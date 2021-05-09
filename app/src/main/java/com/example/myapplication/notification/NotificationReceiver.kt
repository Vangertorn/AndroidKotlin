package com.example.myapplication.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
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
            if (mNotificationManager.getNotificationChannel("NOTIFICATION_CHANNEL${context.packageName}") == null) {
                val notificationChannel = NotificationChannel(
                    "NOTIFICATION_CHANNEL${context.packageName}",
                    "NOTIFICATION_CHANNEL${context.packageName}",
                    NotificationManager.IMPORTANCE_HIGH
                )
                mNotificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val mBuilder = NotificationCompat.Builder(context,"NOTIFICATION_CHANNEL${context.packageName}" )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(intent.getStringExtra(NotificationRepository.PLANNER_APP_NOTIFICATION_USER))
            .setContentText(intent.getStringExtra(NotificationRepository.PLANNER_APP_NOTIFICATION_TEXT))
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        mNotificationManager.notify(0, mBuilder.build())
    }
}