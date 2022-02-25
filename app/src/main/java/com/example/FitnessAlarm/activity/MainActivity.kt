package com.example.FitnessAlarm.activity

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.FitnessAlarm.broadCastReceiver.AlarmReceiver
import com.example.FitnessAlarm.fragments.AlarmSettingFragment
import com.example.FitnessAlarm.CountAlgorithm.PushupCounter
import com.example.FitnessAlarm.CountAlgorithm.SquatCounter
import com.example.FitnessAlarm.CountAlgorithm.WorkoutCounter
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sharedPreferenceUtils : SharedPreferenceUtils = SharedPreferenceUtils(this)
        val alarmData : AlarmData =  sharedPreferenceUtils.getAlarmDataFromSharedPreference()
        setWorkoutCounter(alarmData.workout)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    // 설정된 운동 종류에 따라 운동 카운트 알고리즘을 생성
    private fun setWorkoutCounter(workout : String)
    {
        if (workout == "스쿼트") {
            workoutCounter = SquatCounter()
        }

        else if (workout == "팔굽혀펴기") {
            workoutCounter = PushupCounter()
        }
    }

    companion object
    {
        const val Filename = "prefs"
        const val SHARED_PREFERENCE_NAME = "time"
        const val HOUR_KEY = "hour"
        const val MINUTE_KEY = "minute"
        const val WORKOUT_KEY = "workout"
        const val REPETITION_KEY = "repetition"
        const val ONOFF_KEY = "onOff"
        const val TITLE_KEY = "title"
        const val TONE_KEY = "tone"
        const val MON_KEY = "mon"
        const val TUE_KEY = "tue"
        const val WED_KEY = "wed"
        const val THU_KEY = "thu"
        const val FRI_KEY = "fri"
        const val SAT_KEY = "sat"
        const val SUN_KEY = "sun"
        const val RINGTITLE_KEY = "ring"
        const val VOLUME_KEY = "volume"
        var workoutCounter : WorkoutCounter = SquatCounter()
    }

}

