package com.example.websiteobserver.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.websiteobserver.services.ObserverService

class ObserverRestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        context.startForegroundService(Intent(context, ObserverService::class.java))
    }
}