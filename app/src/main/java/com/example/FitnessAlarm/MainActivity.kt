package com.example.FitnessAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import com.example.FitnessAlarm.CountAlgorithm.PushupCounter
import com.example.FitnessAlarm.CountAlgorithm.SquatCounter
import com.example.FitnessAlarm.CountAlgorithm.WorkoutCounter
import com.example.FitnessAlarm.data.AlarmData


class MainActivity : AppCompatActivity(){

    companion object
    {
        const val Filename = "prefs"
        const val SHARED_PREFERENCE_NAME = "time"
        const val HOUR_KEY = "hour"
        const val MINUTE_KEY = "minute"
        const val WORKOUT_KEY = "workout"
        const val REPETITION_KEY = "repetition"
        const val ONOFF_KEY = "onOff"
        const val ALARM_REQUEST_CODE = 222

        var workoutCounter : WorkoutCounter = SquatCounter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()

        val set_btn : Button = findViewById(R.id.alarm_set_btn)
        val alarm_switch : Switch = findViewById(R.id.alarm_switch)

        var alarmData : AlarmData = fetchSharedPreferences()

        settingAlarm()

        changeAlarmSetting(alarmData,set_btn)
        changeAlarmOnOff(alarmData,alarm_switch)


    }

    override fun onPause() {
        super.onPause()
    }

    // 설정된 운동 종류에 따라 운동 카운트 알고리즘을 생성
    private fun setWorkoutCounter(workout : String)
    {

        if (workout == "squat") {
            MainActivity.workoutCounter = SquatCounter()
        }

        else if (workout == "pushup") {
            MainActivity.workoutCounter = PushupCounter()
        }
        // setWorkoutCounter()를 실행할 경우, workOutCounter의 변수값이 default로 초기화되므로 적절히 값을 바꿔주어야 한다.
        MainActivity.workoutCounter.completeGoal = getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE).getInt(
            REPETITION_KEY,2)
    }

    // 버튼 누르면 AlarmSetting 변경 Layout으로 이동
    private fun changeAlarmSetting(alarmData : AlarmData, set_btn : Button )
    {
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
    private fun changeAlarmOnOff(alarmData : AlarmData, alarm_switch: Switch)
    {
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

    private fun changeAlarmMusic()
    {
        val bellLayout : ViewGroup = findViewById(R.id.bell_layout)
        // 레이아웃 누를 시 벨소리 변경 화면으로 전환
        bellLayout.setOnClickListener(){

        }
    }
    // SharedPrefernces 데이터 불러오기
    private fun fetchSharedPreferences() : AlarmData
    {
        val sharedPreference : SharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        var fetchHour : String  = sharedPreference!!.getString(HOUR_KEY,"8")!!
        var fetchMinute : String = sharedPreference!!.getString(MINUTE_KEY,"00")!!
        var fetchWorkout : String = sharedPreference!!.getString(WORKOUT_KEY,"squat")!!
        var fetchRepetition : Int = sharedPreference!!.getInt(REPETITION_KEY,2)!!
        var fetchOnOff : Boolean = sharedPreference.getBoolean(ONOFF_KEY,true)

        val alarmInfo : AlarmData = AlarmData(fetchHour,fetchMinute,fetchWorkout,fetchRepetition,fetchOnOff)

        return alarmInfo
    }

    private fun saveAlarmData(hour:String,min:String,workout:String,repCnt : Int,onOff:Boolean) : AlarmData
    {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        val DataModel = AlarmData(
            hour = hour,
            min = min,
            workout = workout,
            repCnt = repCnt,
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

        MainActivity.workoutCounter.completeGoal = newAlarmData.getRepCnt

        Log.i("Hour test","Hour :" + newAlarmData.getHour)
        Log.i("Minute test","Minute :" + newAlarmData.getMinute)
        Log.i("Onoff test","Onoff :" + newAlarmData.getOnOff)

        // Calendar에 알람 설정 시간을 입력
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


            // 캘린더에 입력된 알람 시간에 실행되는 알람매니저 등록
            val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val receiverIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQUEST_CODE,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            setWorkoutCounter(sharedPreference.getString(WORKOUT_KEY,"squat")!!)
            /*
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            */

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

