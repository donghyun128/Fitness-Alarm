package com.example.FitnessAlarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.FitnessAlarm.broadCastReceiver.AlarmReceiver
import java.util.*

data class AlarmData(
    val alarmId : Int,
    var hour: Int,
    var min: Int,
    var workout: String,
    var repCnt : Int,
    var onOff: Boolean,
    var monday : Boolean,
    var tuesday : Boolean,
    var wednesday : Boolean,
    var thursday : Boolean,
    var friday : Boolean,
    var saturday : Boolean,
    var sunday : Boolean,
    var title : String,
    var tone : String,
    var ringTitle : String
) {
    val timeToText : String
        get()
        {
            val h = if (hour.toInt() > 12 ) (hour.toInt() - 12).toString() else hour
            return "$h : $min "
        }
    val getAMPM : String
        get()
        {
            return if (hour.toInt() < 12) "AM" else "PM"
        }

    val getOnOff : Boolean
        get()
        {
            return onOff
        }

    val getHour : Int
        get()
        {
            return hour
        }

    val getMinute : Int
        get()
        {
            return min
        }

    val getWorkOut : String
        get()
        {
            return workout
        }

    val getRepCnt : Int
        get()
        {
            return repCnt
        }
    val getMonday : Boolean
        get()
        {
            return monday
        }

    val getTuesday : Boolean
        get()
        {
            return tuesday
        }

    val getWednesday : Boolean
        get()
        {
            return wednesday
        }

    val getThursday : Boolean
        get()
        {
            return thursday
        }

    val getFriday : Boolean
        get()
        {
            return friday
        }

    val getSaturday : Boolean
        get()
        {
            return saturday
        }

    val getSunday : Boolean
        get()
        {
            return sunday
        }

    val getTitle : String
        get()
        {
            return title
        }

    val getTone : String
        get()
        {
            return tone
        }

    val getRingTitle : String
    get()
    {
        return ringTitle
    }

    // 알람 설정
    public fun setAlarm(context: Context?)
    {
        val alarmManager : AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent : Intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent : PendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0)

        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,min)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )

        val toastText : String = String.format("%02d:%02d 알람이 설정되었습니다.",hour,min)
        Toast.makeText(context,toastText, Toast.LENGTH_SHORT).show()
    }

    public fun cancelAlarm(context: Context?){

        val alarmManager : AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent : Intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent : PendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0)
        alarmManager.cancel(alarmPendingIntent)
        this.onOff = false
        val toastText : String = String.format("%02d:%02d 알람이 취소되었습니다.",hour,min)
        Toast.makeText(context,toastText, Toast.LENGTH_SHORT).show()

    }
}