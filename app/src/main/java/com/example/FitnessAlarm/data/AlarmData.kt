package com.example.FitnessAlarm.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

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



}