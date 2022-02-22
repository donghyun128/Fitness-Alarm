package com.example.FitnessAlarm.utils

import android.view.View
import com.example.FitnessAlarm.data.AlarmData

public interface OnToggleAlarmListener {
    fun onToggle(alarm : AlarmData)
    fun onDelete(alarm : AlarmData)
    fun onitemClick(alarm : AlarmData,view : View)

}