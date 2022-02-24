package com.example.FitnessAlarm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App :Application(){
    companion object
    {
        const val CHANNEL_ID = "ALARM_CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel : NotificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name)+"Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager : NotificationManager = getSystemService(NotificationManager::class.java);
            manager.createNotificationChannel(serviceChannel);
        }
    }
 }
