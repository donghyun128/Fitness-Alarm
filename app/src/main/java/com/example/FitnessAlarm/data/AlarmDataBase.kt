package com.example.FitnessAlarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [AlarmData::class], version = 1, exportSchema = false)
abstract class AlarmDataBase : RoomDatabase() {

    abstract fun alarmDao() : AlarmDao

    companion object
    {
        private var instance : AlarmDataBase? = null
        private val NUMBER_OF_THREADS : Int = 4
        final val databaseWriteExecutor : ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Synchronized
        fun getInstance(context : Context) : AlarmDataBase?
        {
            if (instance == null){
                synchronized(AlarmDataBase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDataBase::class.java,
                        "alarm-database"
                    ).build()
                }
            }
            return instance
        }
    }

}