package com.example.FitnessAlarm.broadCastReceiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.service.AlarmService
import com.example.FitnessAlarm.service.RescheduleAlarmService

class AlarmReceiver : BroadcastReceiver() {


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "222"
        const val NOTIFICATION_ID = 222
    }

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferenceUtils: SharedPreferenceUtils = SharedPreferenceUtils(context)
        Log.i("test", "test activity")
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.action)) {
            startResheduleAlarmService(context)
        } else {
            Log.d("onReceive", "startAlarmService")
            val alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()
            startAlarmService(context, alarmData)
        }


    }

    fun startAlarmService(context: Context, alarmData: AlarmData) {
        Log.d("startAlarmService", "startAlarmService")

        val intentService: Intent = Intent(context, AlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    fun startResheduleAlarmService(context: Context) {
        val intentService: Intent = Intent(context, RescheduleAlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}


