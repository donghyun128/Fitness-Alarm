

package com.example.FitnessAlarm.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.FitnessAlarm.AlarmReceiver
import com.example.FitnessAlarm.R
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "alarm_table")
@Parcelize
class AlarmData(
    @PrimaryKey
    var alarmId : Int,
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
    var tone : String

) : Parcelable {
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
    val getAlarmId : Int
        get()
        {
            return alarmId
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

    val getOnOff : Boolean
        get()
        {
            return onOff
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


    public fun setAlarm(context : Context)
    {
        val alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent : Intent = Intent(context, AlarmReceiver::class.java)
        val bundle : Bundle = Bundle()
        bundle.putParcelable(context.getString(R.string.bundle_alarm_obj),bundle)
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
        Toast.makeText(context,toastText,Toast.LENGTH_SHORT).show()
    }

    public fun cancelAlarm(context :Context){

        val alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent : Intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent : PendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0)
        alarmManager.cancel(alarmPendingIntent)
        this.onOff = false
        val toastText : String = String.format("%02d:%02d 알람이 취소되었습니다.",hour,min)
        Toast.makeText(context,toastText,Toast.LENGTH_SHORT).show()

    }

}
/*
package com.example.FitnessAlarm.data

data class AlarmData(
    var hour: String,
    var min: String,
    var workout: String,
    var repCnt : Int,
    var onOff: Boolean

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

    val getHour : String
        get()
        {
            return hour
        }

    val getMinute : String
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

}

 */