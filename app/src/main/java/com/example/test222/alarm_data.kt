package com.example.test222

data class alarm_data(
    val hour: Int,
    val min: Int,
    var onOff : Boolean
) {
    val timeToText : String
        get()
        {
            val h = "%02d".format(if (hour<12) hour else hour-12)
            val m = "%02d".format(min)

            return "$h:$m"
        }
    val getAmPm : String
        get()
        {
            return if (hour < 12) "AM" else "PM"
        }

    val GetOnOff : Boolean
        get()
        {
            return onOff
        }

    val GetHour : Int
        get()
        {
            return hour
        }

    val GetMinute : Int
        get()
        {
            return min
        }

}