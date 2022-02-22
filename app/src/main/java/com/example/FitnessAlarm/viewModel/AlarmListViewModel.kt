package com.example.FitnessAlarm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.AlarmReposit

class AlarmListViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmReposit = AlarmReposit(application)
    private val alarmLiveData = alarmReposit.getAlarmLiveData()

    public fun update(alarmData : AlarmData)
    {
        alarmReposit.update(alarmData)
    }

    public fun getAlarmLiveData() : LiveData<List<AlarmData>>
    {
        return alarmLiveData
    }

    public fun delete(alarId : Int)
    {
        alarmReposit.delete(alarId)
    }

}