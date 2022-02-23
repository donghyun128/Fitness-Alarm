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
import com.example.FitnessAlarm.databinding.AlarmItemBinding
import com.example.FitnessAlarm.utils.OnToggleAlarmListener

class AlarmRecycleViewAdapter(listener: OnToggleAlarmListener) : RecyclerView.Adapter<AlarmViewHolder>() {

    private lateinit var alarms : List<AlarmData>
    private lateinit var listener : OnToggleAlarmListener
    private lateinit var alarmItemBinding : AlarmItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        alarmItemBinding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlarmViewHolder(alarmItemBinding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm : AlarmData = alarms.get(position)
        holder.bind(alarm,listener)
    }

    override fun getItemCount(): Int {
        return 1
        //return alarms.size
    }

    fun setAlarms(alarms : List<AlarmData>)
    {
        this.alarms = alarms
        notifyDataSetChanged()
    }
}