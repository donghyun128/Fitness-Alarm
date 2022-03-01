package com.example.FitnessAlarm.broadCastReceiver

import android.app.*
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.service.AlarmService
import com.example.FitnessAlarm.service.RescheduleAlarmService
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        val now = System.currentTimeMillis()
        val currentHour = SimpleDateFormat("HH").format(now).toInt()
        val currentMinute = SimpleDateFormat("mm").format(now).toInt()

        Log.d("current",currentHour.toString() + " "  + currentMinute.toString())
        val powerManager  = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wakeLock=
            powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,"Myapp:wakeLock")


        val sharedPreferenceUtils: SharedPreferenceUtils = SharedPreferenceUtils(context)
        val alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()

        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val toastText : String = String.format("Alarm reboot")
            Toast.makeText(context,toastText,Toast.LENGTH_LONG).show()
            startRescheduleAlarmService(context)
        }

        else {
            Log.d("current",alarmData.getHour.toString() + " "  + alarmData.getMinute.toString())
            // 알람데이터의 시 / 분 과 현재의 시 / 분이 일치할 때만 알람 서비스를 시작한다.
            if (alarmData.getHour == currentHour && alarmData.getMinute == currentMinute)
            {
                Log.d("current",alarmData.getHour.toString() + " "  + alarmData.getMinute.toString())
                wakeLock.acquire()
                startAlarmService(context, alarmData)
                wakeLock.release()
            }

        }


    }

    private fun startAlarmService(context: Context, alarmData: AlarmData) {
        Log.d("startAlarmService", "startAlarmService")
        val intentService: Intent = Intent(context, AlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }


    }

    private fun startRescheduleAlarmService(context: Context) {
        val intentService: Intent = Intent(context, RescheduleAlarmService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}


