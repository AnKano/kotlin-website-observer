package com.example.websiteobserver.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.websiteobserver.InitialActivity
import com.example.websiteobserver.R

class FailureReceiver: BroadcastReceiver() {
    private val oneShotNotificationChannelId = "com.example.websiteobserver_oneShot"

    override fun onReceive(context: Context, intent: Intent?) {
        createChannel(context, oneShotNotificationChannelId, "Website One Shot Channel")

        val serviceChannel = NotificationChannel(oneShotNotificationChannelId, "Website One Shot Channel", NotificationManager.IMPORTANCE_DEFAULT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannel(serviceChannel)

        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, InitialActivity::class.java), 0
        )

        val message = when(val failureCount = intent?.extras?.get("COUNT")) {
            1 ->  "$failureCount unavailable host found"
            else -> "$failureCount unavailable hosts found"
        }

        val mBuilder = NotificationCompat.Builder(context, oneShotNotificationChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Website Observer")
            .setContentText(message)
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun createChannel(context: Context, id: String, name: String) {
        val serviceChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannel(serviceChannel)
    }
}