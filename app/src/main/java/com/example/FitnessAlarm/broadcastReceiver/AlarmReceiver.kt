package com.example.FitnessAlarm.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.service.AlarmService
import com.example.FitnessAlarm.service.ResetAlarmService
import android.widget.Toast
import com.example.FitnessAlarm.R
import java.util.*


class AlarmReceiver : BroadcastReceiver() {


    lateinit var alarmData : AlarmData

    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.action)){
            startResetAlarmService(context)
        }

        else{
            val bundle : Bundle? = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj))
            if (bundle!= null)
                alarmData = bundle.getParcelable<AlarmData>(context.getString(R.string.arg_alarm_obj))!!
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

            if (alarmData != null)
            {
                startAlarmService(context,alarmData)
            }
        }

    }

    fun isAlarmToday(alarmData: AlarmData) : Boolean
    {
        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val today : Int = calendar.get(Calendar.DAY_OF_WEEK)

        when (today)
        {
            Calendar.MONDAY ->
            {
                if (alarmData.getMonday)
                    return true
                return false
            }

            Calendar.TUESDAY ->
            {
                if (alarmData.getTuesday)
                    return true
                return false
            }
            Calendar.WEDNESDAY ->
            {
                if (alarmData.getWednesday)
                    return true
                return false
            }
            Calendar.THURSDAY ->
            {
                if (alarmData.getThursday)
                    return true
                return false
            }
            Calendar.FRIDAY ->
            {
                if (alarmData.getFriday)
                    return true
                return false
            }
            Calendar.SATURDAY ->
            {
                if (alarmData.getSaturday)
                    return true
                return false
            }
            Calendar.SUNDAY ->
            {
                if (alarmData.getSunday)
                    return true
                return false
            }
            else ->
                return false
        }
    }

    fun startAlarmService(context: Context, alarmData : AlarmData)
    {
        val intentService : Intent = Intent(context,AlarmService::class.java)
        val bundle = Bundle()
        bundle.putParcelable(context.getString(R.string.arg_alarm_obj),alarmData)
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj),bundle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    fun startResetAlarmService(context: Context)
    {
        val intentService : Intent = Intent(context,ResetAlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }






}

