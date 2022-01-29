package com.example.test222
import android.content.Context
import android.content.SharedPreferences
import com.example.test222.alarm_data

class dataSharedPreference(context: Context) {

    private val Filename = "prefs"
    private val SHARED_PREFERENCE_NAME = "time"
    private val HOUR_KEY = "hour"
    private val MINUTE_KEY = "minute"
    private val ONOFF_KEY = "onOff"
    private val ALARM_REQUEST_CODE = 222
    val sharedprefernces : SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)

    private fun saveAlarmData(hour:Int,min:Int,onOff:Boolean) : alarm_data
    {
        val DataModel = alarm_data(
            hour = hour,
            min = min,
            onOff = onOff
        )

        // 키와 값 전달
        with(sharedprefernces.edit())
        {
            putBoolean(ONOFF_KEY,DataModel.GetOnOff)
            putInt(HOUR_KEY,DataModel.GetHour)
            putInt(MINUTE_KEY,DataModel.GetMinute)
            commit()
        }

        return DataModel

    }


}