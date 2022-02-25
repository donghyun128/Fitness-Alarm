package com.example.FitnessAlarm.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.FitnessAlarm.App
import com.example.FitnessAlarm.App.Companion.CHANNEL_ID
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.activity.RingActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import java.io.IOException
import java.lang.Exception
import android.os.IBinder as IBinder1

class AlarmService : Service() {

    lateinit var mediaPlayer : MediaPlayer
    lateinit var vibrator : Vibrator
    lateinit var alarmData : AlarmData
    lateinit var ringtone : Uri
    lateinit var  sharedPreferenceUtils: SharedPreferenceUtils

    override fun onBind(p0: Intent?): IBinder1? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = true
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.baseContext, RingtoneManager.TYPE_ALARM)
        sharedPreferenceUtils = SharedPreferenceUtils(this)

    }

    // 서비스가 실행될 때 실행
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d("AlarmService","onStartCommand")

        // 서비스가 실행되면 CameraActivity로 이동
        val notificationIntent: Intent = Intent(this, CameraActivity::class.java)
        notificationIntent.setAction(Intent.ACTION_MAIN)
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        // 화면이 없는데에서 intent를 띄울 수 있게한다.
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // intent 위에 다른 화면이 있으면 제거한다.
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // 만들어져 있는 화면을 재활용
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            0
        )


        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()

        var alarmTitle: String = alarmData.getTitle

        if (alarmData != null) {
            alarmTitle = alarmData.getTitle
            try {
                mediaPlayer.setDataSource(this.baseContext, Uri.parse(alarmData.getTone))
                mediaPlayer.setVolume(alarmData.volume.toFloat(),alarmData.volume.toFloat())
                mediaPlayer.prepareAsync()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        val remoteViews : RemoteViews = RemoteViews(packageName,R.layout.activity_ring)

        remoteViews.setTextViewText(R.id.notification_alarm_time,alarmData.timeToText)
        remoteViews.setTextViewText(R.id.notification_rep_count,alarmData.getWorkOut + " " +  alarmData.getRepCnt + "회")

        val notification : Notification = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Ringing")
                .setContentText(alarmTitle)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent,true)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.clock_image)
                .build()

        Log.d("AlarmService : ","mediaPlayer 재생")
        mediaPlayer.setOnPreparedListener { mediaPlayer ->

                mediaPlayer.start()

            }

        val vibratePattern = longArrayOf(1,100,1000,10,100)
        val vibrateAmplitude = intArrayOf(50,80,70,80,90)
        val vibrationEffect = VibrationEffect.createWaveform(vibratePattern,vibrateAmplitude,0)
        vibrator.vibrate(vibrationEffect)
        Log.d("AlarmService : ","startService")

            /*

            try{
                applicationContext.startActivity(notificationIntent)
                //startActivity(notificationIntent)
                //startForegroundService(notificationIntent)
                //pendingIntent.send()
            } catch (e : Exception) {
                e.printStackTrace()
            }
            */

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1,notification)
            startForeground(1,notification)
            return START_REDELIVER_INTENT

        }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
}




