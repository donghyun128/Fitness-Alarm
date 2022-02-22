package com.example.FitnessAlarm.adapter

import android.content.Context
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.databinding.ActivityMainBinding
import com.example.FitnessAlarm.utils.DayUtil
import com.example.FitnessAlarm.utils.OnToggleAlarmListener

class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val alarmTime: TextView = itemView.findViewById<TextView>(R.id.item_alarm_time)
    private val alarmTitle: TextView = itemView.findViewById<TextView>(R.id.item_alarm_title)
    private val alarmOnOff: Switch = itemView.findViewById<Switch>(R.id.item_alarm_started)
    private val alarmDay: TextView = itemView.findViewById<TextView>(R.id.item_alarm_day)

    fun bind(alarmData : AlarmData, context : Context, listener : OnToggleAlarmListener){
        val timeText : String = String.format("%02d:%02d",alarmData.getHour,alarmData.getMinute)
        alarmTime.setText(timeText)
        alarmTitle.setText(alarmData.title)
        alarmDay.setText(DayUtil().getDay(alarmData.getHour,alarmData.getMinute))
        alarmOnOff.isChecked = alarmData.getOnOff

    }
}