package com.example.FitnessAlarm.data
import android.content.Context
import android.content.SharedPreferences
import com.example.FitnessAlarm.activity.MainActivity.Companion.FRI_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.HOUR_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.MINUTE_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.MON_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.ONOFF_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.REPETITION_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.RINGTITLE_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.SAT_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.FitnessAlarm.activity.MainActivity.Companion.SUN_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.THU_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.TITLE_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.TONE_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.TUE_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.VOLUME_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.WED_KEY
import com.example.FitnessAlarm.activity.MainActivity.Companion.WORKOUT_KEY


class SharedPreferenceUtils(context: Context?) {

    val sharedpreferences : SharedPreferences = context!!.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)

    public fun setDataPreference(alarmData: AlarmData)
    {

        // 키와 값 전달
        with(sharedpreferences.edit())
        {
            putInt(HOUR_KEY,alarmData.getHour)
            putInt(MINUTE_KEY,alarmData.getMinute)
            putString(WORKOUT_KEY,alarmData.getWorkOut)
            putBoolean(ONOFF_KEY,alarmData.getOnOff)
            putInt(REPETITION_KEY,alarmData.getRepCnt)
            putString(TITLE_KEY,alarmData.getTitle)
            putString(TONE_KEY,alarmData.getTone)
            putBoolean(MON_KEY,alarmData.getMonday)
            putBoolean(TUE_KEY,alarmData.getTuesday)
            putBoolean(WED_KEY,alarmData.getWednesday)
            putBoolean(THU_KEY,alarmData.getThursday)
            putBoolean(FRI_KEY,alarmData.getFriday)
            putBoolean(SAT_KEY,alarmData.getSaturday)
            putBoolean(SUN_KEY,alarmData.getSunday)
            putString(RINGTITLE_KEY,alarmData.getRingTitle)
            putInt(VOLUME_KEY,alarmData.getVolume)
            commit()
        }

    }

    public fun getAlarmDataFromSharedPreference() : AlarmData
    {
        val hour = sharedpreferences.getInt(HOUR_KEY,12)
        val minute = sharedpreferences.getInt(MINUTE_KEY,0)
        val workout = sharedpreferences.getString(WORKOUT_KEY,"스쿼트")!!
        val repetition = sharedpreferences.getInt(REPETITION_KEY,0)
        val title = sharedpreferences.getString(TITLE_KEY,"Alarm")!!
        val tone = sharedpreferences.getString(TONE_KEY,"default")!!
        val onOff = sharedpreferences.getBoolean(ONOFF_KEY,true)
        val mon = sharedpreferences.getBoolean(MON_KEY,true)
        val tue = sharedpreferences.getBoolean(TUE_KEY,true)
        val wed = sharedpreferences.getBoolean(WED_KEY,true)
        val thu = sharedpreferences.getBoolean(THU_KEY,true)
        val fri = sharedpreferences.getBoolean(FRI_KEY,true)
        val sat = sharedpreferences.getBoolean(SAT_KEY,true)
        val sun = sharedpreferences.getBoolean(SUN_KEY,true)
        val ringTitle = sharedpreferences.getString(RINGTITLE_KEY,"")!!
        val volume = sharedpreferences.getInt(VOLUME_KEY,10)

        val alarmData :AlarmData = AlarmData(1,hour,minute,workout,repetition,onOff,mon,tue,wed,thu,fri,sat,sun,title,tone,ringTitle,volume)

        return alarmData
    }


}