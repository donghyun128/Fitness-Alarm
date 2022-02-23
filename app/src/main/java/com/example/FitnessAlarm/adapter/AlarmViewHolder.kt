package com.example.FitnessAlarm.adapter

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.databinding.ActivityMainBinding
import com.example.FitnessAlarm.databinding.AlarmItemBinding
import com.example.FitnessAlarm.utils.DayUtil
import com.example.FitnessAlarm.utils.OnToggleAlarmListener


// recyclerView의 요소들에 관한 내용을 서술

class AlarmViewHolder(alarmItemBinding: AlarmItemBinding) : RecyclerView.ViewHolder(alarmItemBinding.root) {

    private val alarmTime: TextView = alarmItemBinding.itemAlarmTime
    private val alarmTitle: TextView = alarmItemBinding.itemAlarmTitle
    private val alarmStarted: Switch = alarmItemBinding.itemAlarmStarted
    private val alarmDay: TextView = alarmItemBinding.itemAlarmDay
    private val alarmDeleteButton : ImageButton = alarmItemBinding.itemDeleteButton

    fun bind(alarmData : AlarmData, listener : OnToggleAlarmListener){
        val timeText : String = String.format("%02d:%02d",alarmData.getHour,alarmData.getMinute)
        alarmTime.setText(timeText)
        alarmDay.setText(DayUtil().getDay(alarmData.getHour,alarmData.getMinute))
        alarmStarted.isChecked = alarmData.getOnOff

        if (alarmData.getTitle.length != 0)
        {
            alarmTitle.setText(alarmData.getTitle)
        }
        else
        {
            alarmTitle.setText("Alarm")
        }

        alarmStarted.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown || buttonView.isPressed)
                listener.onToggle(alarmData)
        }

        alarmDeleteButton.setOnClickListener { view ->
            listener.onDelete(alarmData)
        }


    }

}