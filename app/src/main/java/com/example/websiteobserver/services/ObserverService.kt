package com.example.websiteobserver.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.websiteobserver.database.WebsiteRoomDatabase
import com.example.websiteobserver.receivers.FailureReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class ObserverService : Service() {
    private val longTaskNotificationChannelId = "com.example.websiteobserver_foreground"

    private val serviceScope = CoroutineScope(SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundTask()
    }

    private fun startForegroundTask() {
        createNotificationChannels()

        val notificationBuilder = NotificationCompat.Builder(this, longTaskNotificationChannelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_NONE)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        performWebsiteObserving()
        return START_STICKY
    }

    private fun performWebsiteObserving() {
        val context = this

        serviceScope.launch {
            val client = OkHttpClient()

            val database = WebsiteRoomDatabase.getDatabase(context, serviceScope)
            val dao = database.dao()
            val websites = dao.getEverythingAsList()

            var failCounter = 0

            websites.forEach { item ->
                val timestamp = System.currentTimeMillis()
                item.status = "pending"
                item.lastKnownPing = null
                dao.insertEntry(item)

                val request = Request.Builder().url(item.url).get().build()
                try {
                    client.newCall(request).execute().use { response ->
                        item.lastKnownPing = response.receivedResponseAtMillis - timestamp
                        item.status = "available"
                        serviceScope.launch { dao.insertEntry(item) }
                    }
                } catch (e: Exception) {
                    failCounter++

                    item.lastKnownPing = null
                    item.status = "not available"
                    serviceScope.launch { dao.insertEntry(item) }
                }
            }

            if (failCounter > 0)
                notifyAboutFailure(failCounter)

            stopSelf()
        }
    }

    private fun notifyAboutFailure(count: Int) {
        val broadcastIntent = Intent()
        broadcastIntent.setClass(this, FailureReceiver::class.java)
        broadcastIntent.putExtra("COUNT", count)
        this.sendBroadcast(broadcastIntent)
    }

    private fun createChannel(id: String, name: String) {
        val serviceChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }

    private fun createNotificationChannels() {
        createChannel(longTaskNotificationChannelId, "Foreground Website Observer")
    }
}
