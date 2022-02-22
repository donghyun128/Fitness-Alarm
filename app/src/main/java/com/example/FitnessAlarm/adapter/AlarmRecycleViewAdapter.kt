package com.example.FitnessAlarm.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.utils.OnToggleAlarmListener

class AlarmRecycleViewAdapter(val context : Context,val alarms : List<AlarmData>) : RecyclerView.Adapter<AlarmViewHolder>() {

    lateinit var tmp_alarms : List<AlarmData>
    private lateinit var listener : OnToggleAlarmListener

    fun createViewHolder(listener: OnToggleAlarmListener)
    {
        this.tmp_alarms = ArrayList<AlarmData>()
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.alarm_item,parent,false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm : AlarmData = alarms.get(position)
        holder.bind(alarms[position],context,listener)
    }

    override fun getItemCount(): Int {
        return alarms.size
    }
}