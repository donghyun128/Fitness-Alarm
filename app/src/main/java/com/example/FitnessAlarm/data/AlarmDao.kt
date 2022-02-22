package com.example.FitnessAlarm.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.FitnessAlarm.data.AlarmData

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarm : AlarmData)

    @Query("DELETE FROM alarm_table")
    fun deleteAll()

    @Query("SELECT * FROM alarm_table ORDER BY alarmId ASC")
    fun getAlarms() : LiveData<List<AlarmData>>

    @Update
    fun update(alarmData : AlarmData)

    @Query("Delete from alarm_table where alarmId = :alarmID")
    fun delete(alarmID : Int)


}