package com.example.FitnessAlarm.data

import android.app.Application
import androidx.lifecycle.LiveData

class AlarmReposit(application : Application) {

    val db : AlarmDataBase? = AlarmDataBase.getInstance(application)

    private var alarmDao : AlarmDao = db!!.alarmDao()

    private var alarmLiveData : LiveData<List<AlarmData>> = alarmDao.getAlarms()

    public fun insert(alarmData : AlarmData)
    {
        AlarmDataBase.databaseWriteExecutor.execute(
            {
                alarmDao.insert(alarmData)
            }
        )

    }

    public fun update(alarmData : AlarmData)
    {
        AlarmDataBase.databaseWriteExecutor.execute(
            {
                alarmDao.update(alarmData)
            }
        )
    }

    public fun getAlarmLiveData() : LiveData<List<AlarmData>>
    {
        return alarmLiveData
    }

    public fun delete(alarmId : Int)
    {
        AlarmDataBase.databaseWriteExecutor.execute(
            {
                alarmDao.delete(alarmId)
            }
        )
    }


}