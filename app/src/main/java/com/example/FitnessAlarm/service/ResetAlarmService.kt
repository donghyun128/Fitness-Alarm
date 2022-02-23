package com.example.FitnessAlarm.service

import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.FitnessAlarm.model.AlarmData
import com.example.FitnessAlarm.model.AlarmRepository

class ResetAlarmService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val alarmRepository : AlarmRepository = AlarmRepository(application)

        alarmRepository.getAlarmLiveData().observe(this, Observer<List<AlarmData>>()
        {
                alarms ->
            {
                for (alarm : AlarmData in alarms )
                {
                    if (alarm.getOnOff)
                        alarm.setAlarm(applicationContext)
                }
            }
        }

        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent : Intent) : IBinder?
    {
        super.onBind(intent)
        return null
    }


}