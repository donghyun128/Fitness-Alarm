package com.example.FitnessAlarm.utils

import android.os.Build
import android.widget.TimePicker

public final class TimePickerUtils {

    public fun getTimePickerHour(tp : TimePicker) : Int
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return tp.hour
        } else {
            return tp.currentHour
        }
    }

    public fun getTimePickerMinute(tp : TimePicker) : Int
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.minute
        } else {
            return tp.currentMinute
        }
    }
}