package com.example.FitnessAlarm.utils

import java.util.*

public class DayUtil {

    public fun findDay(day : Int) : String
    {
        when(day){
            Calendar.SUNDAY ->
                return "Sunday"
            Calendar.MONDAY ->
                return "Monday"
            Calendar.TUESDAY ->
                return "Tuesday"
            Calendar.WEDNESDAY ->
                return "Wednesday"
            Calendar.THURSDAY ->
                return "Thursday"
            Calendar.FRIDAY ->
                return "Friday"
            Calendar.SATURDAY ->
                return "Saturday"
            else -> return "요일을 찾을 수 없습니다"
        }
    }

    public fun getDay(hour : Int, minute : Int) : String
    {
        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)

        if (calendar.timeInMillis <= System.currentTimeMillis())
        {
            return "Tomorrow"
        }
        else
            return "Today"
    }


}