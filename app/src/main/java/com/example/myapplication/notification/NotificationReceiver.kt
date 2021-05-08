package com.example.myapplication.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
        val mBuilder = NotificationCompat.Builder(context, context.packageName)
            .setSmallIcon(R.drawable.ic_cloud)
            .setContentTitle(intent.getStringExtra(NotificationRepository.PLANNER_APP_NOTIFICATION_USER))
            .setContentText(intent.getStringExtra(NotificationRepository.PLANNER_APP_NOTIFICATION_TEXT))
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(0, mBuilder.build())
    }
}