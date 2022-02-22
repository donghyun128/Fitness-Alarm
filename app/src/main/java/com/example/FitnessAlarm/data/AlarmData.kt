package com.example.FitnessAlarm.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlarmData(
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

    val isMonday : Boolean
        get()
        {
            return monday
        }

    val isTuesday : Boolean
        get()
        {
            return tuesday
        }

    val isWednesday : Boolean
        get()
        {
            return wednesday
        }

    val isThursday : Boolean
        get()
        {
            return thursday
        }

    val isFriday : Boolean
        get()
        {
            return friday
        }

    val isSaturday : Boolean
        get()
        {
            return saturday
        }

    val isSunday : Boolean
        get()
        {
            return sunday
        }

    val getTone : String
        get()
        {
            return tone
        }



}