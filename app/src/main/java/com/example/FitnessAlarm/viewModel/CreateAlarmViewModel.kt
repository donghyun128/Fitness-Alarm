package com.example.FitnessAlarm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.AlarmReposit

class CreateAlarmViewModel(application: Application) : AndroidViewModel(application) {

  private val alarmReposit = AlarmReposit(application)

  public fun insert(alarmData : AlarmData)
  {
      alarmReposit.insert(alarmData)
  }

  public fun update(alarmData : AlarmData)
  {
      alarmReposit.update(alarmData)
  }

}