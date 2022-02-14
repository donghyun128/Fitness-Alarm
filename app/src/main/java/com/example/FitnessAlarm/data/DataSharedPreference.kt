package com.example.FitnessAlarm.data
import android.content.Context
import android.content.SharedPreferences
import com.example.FitnessAlarm.MainActivity.Companion.HOUR_KEY
import com.example.FitnessAlarm.MainActivity.Companion.MINUTE_KEY
import com.example.FitnessAlarm.MainActivity.Companion.ONOFF_KEY
import com.example.FitnessAlarm.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.FitnessAlarm.MainActivity.Companion.WORKOUT_KEY


class dataSharedPreference(context: Context) {




    val sharedpreferences : SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
    private fun saveAlarmData(hour:String,min:String,workout:String,repCnt : Int,onOff:Boolean) : AlarmData
    {
        val DataModel = AlarmData(
            hour = hour,
            min = min,
            workout = workout,
            repCnt = repCnt,
            onOff = onOff
        )

        // 키와 값 전달
        with(sharedpreferences.edit())
        {
            putString(HOUR_KEY,DataModel.getHour)
            putString(MINUTE_KEY,DataModel.getMinute)
            putString(WORKOUT_KEY,DataModel.getWorkOut)
            putBoolean(ONOFF_KEY,DataModel.getOnOff)
            commit()
        }

        return DataModel

    }


}