package com.example.test222.data
import android.content.Context
import android.content.SharedPreferences
import com.example.test222.MainActivity.Companion.HOUR_KEY
import com.example.test222.MainActivity.Companion.MINUTE_KEY
import com.example.test222.MainActivity.Companion.ONOFF_KEY
import com.example.test222.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.test222.MainActivity.Companion.WORKOUT_KEY


class dataSharedPreference(context: Context) {




    val sharedpreferences : SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
    private fun saveAlarmData(hour:String,min:String,workout:String,onOff:Boolean) : AlarmData
    {
        val DataModel = AlarmData(
            hour = hour,
            min = min,
            workout = workout,
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