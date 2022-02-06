package com.example.test222

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import java.util.*


class MainActivity : AppCompatActivity(){

    companion object
    {
        const val Filename = "prefs"
        const val SHARED_PREFERENCE_NAME = "time"
        const val HOUR_KEY = "hour"
        const val MINUTE_KEY = "minute"
        const val WORKOUT_KEY = "workout"
        const val ONOFF_KEY = "onOff"
        const val ALARM_REQUEST_CODE = 222
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var alarmData : AlarmData  = fetchSharedPreferences()

        changeAlarmSetting(alarmData)
        changeAlarmOnOff(alarmData)

    }


    // 버튼 누르면 AlarmSetting 변경 Layout으로 이동
    private fun changeAlarmSetting(alarmData : AlarmData )
    {
        val set_btn : Button = findViewById(R.id.alarm_set_btn)
        set_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AlarmSetting::class.java)
            startActivity(intent)
        })

        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)

        alarmData.hour = sharedPreference.getString(HOUR_KEY,alarmData.hour)!!
        alarmData.min  = sharedPreference.getString(MINUTE_KEY,alarmData.min)!!

        set_btn.setText(alarmData.timeToText + alarmData.getAMPM)

    }

    // Switch 누르면 AlarmOnOff 설정
    private fun changeAlarmOnOff(alarmData : AlarmData)
    {
        val alarm_switch : Switch = findViewById(R.id.alarm_switch)
        if (alarmData.getOnOff) {
            alarm_switch.isChecked = true
        }
        else {
            alarm_switch.isChecked = false
        }

        alarm_switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener
        {
            val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =

                if (isChecked)
                {
                    settingAlarm()
                }

                else {
                    sharedPreference.edit().putBoolean(ONOFF_KEY,false).apply()
                    cancelAlarm()
                }
        }
        )
    }

    // SharedPrefernces 데이터 불러오기
    private fun fetchSharedPreferences() : AlarmData
    {
        val sharedPreference : SharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        var fetchHour : String  = sharedPreference!!.getString(HOUR_KEY,"8")!!
        var fetchMinute : String = sharedPreference!!.getString(MINUTE_KEY,"00")!!
        var fetchWorkout : String = sharedPreference!!.getString(WORKOUT_KEY,"squat")!!
        var fetchOnOff : Boolean = sharedPreference.getBoolean(ONOFF_KEY,true)

        val alarmInfo : AlarmData = AlarmData(fetchHour,fetchMinute,fetchWorkout,fetchOnOff)

        return alarmInfo
    }

    private fun saveAlarmData(hour:String,min:String,workout:String,onOff:Boolean) : AlarmData
    {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        val DataModel = AlarmData(
            hour = hour,
            min = min,
            workout = workout,
            onOff = onOff
        )

        // 키와 값 전달
        with(sharedPreferences.edit())
        {
            putString(HOUR_KEY,DataModel.getHour)
            putString(MINUTE_KEY,DataModel.getMinute)
            putString(WORKOUT_KEY,DataModel.getWorkOut)
            putBoolean(ONOFF_KEY,DataModel.getOnOff)
            commit()
        }

        return DataModel

    }

    private fun settingAlarm()
    {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        sharedPreference.edit().putBoolean(ONOFF_KEY,true).apply()
        val newAlarmData = fetchSharedPreferences()

        Log.i("Hour test","Hour :" + newAlarmData.getHour)
        Log.i("Minute test","Minute :" + newAlarmData.getMinute)
        Log.i("Onoff test","Onoff :" + newAlarmData.getOnOff)

        if (newAlarmData.getOnOff) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, newAlarmData.getHour.toInt())
                set(Calendar.MINUTE, newAlarmData.getMinute.toInt())
                if (before(Calendar.getInstance()))
                    add(Calendar.DATE, 1)
            }

            Log.i("MONTH ", "MONTH :" + calendar.get(Calendar.MONTH))
            Log.i("DATE ", "DATE :" + calendar.get(Calendar.DATE))


            val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val receiverIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQUEST_CODE,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )


            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        else
        {
            cancelAlarm()
        }

    }

    private fun cancelAlarm()
    {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this,AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }
}

