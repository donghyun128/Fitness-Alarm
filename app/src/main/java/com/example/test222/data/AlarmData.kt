package com.example.test222.data

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