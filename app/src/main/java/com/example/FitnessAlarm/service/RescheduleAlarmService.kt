package com.example.FitnessAlarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.FitnessAlarm.activity.MainActivity
import com.example.FitnessAlarm.data.SharedPreferenceUtils

class RescheduleAlarmService : Service() {

    lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        sharedPreferenceUtils = SharedPreferenceUtils(applicationContext)
        val alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()
        MainActivity.setWorkoutCounter(alarmData.getWorkOut)

        if (alarmData.getOnOff) {
            alarmData.cancelAlarm(applicationContext)
            alarmData.setAlarm(applicationContext)
        }
        //else
        //    alarmData.cancelAlarm(applicationContext)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}