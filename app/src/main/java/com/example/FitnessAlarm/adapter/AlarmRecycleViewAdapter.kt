package com.example.FitnessAlarm.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.AlarmReposit
import com.example.FitnessAlarm.utils.OnToggleAlarmListener

class AlarmRecycleViewAdapter(val context : Context,alarms: List<AlarmData>) : RecyclerView.Adapter<AlarmViewHolder>() {

    private lateinit var alarms : List<AlarmData>
    private lateinit var listener : OnToggleAlarmListener
    private lateinit var alarmReposit : AlarmReposit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item,parent,false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm : AlarmData = alarms.get(position)
        holder.bind(alarms[position],context,listener)
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    fun setAlarms(alarms : List<AlarmData>)
    {
        this.alarms = alarms
        notifyDataSetChanged()
    }
}