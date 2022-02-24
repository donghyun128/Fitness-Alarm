package com.example.FitnessAlarm.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.FitnessAlarm.App
import com.example.FitnessAlarm.App.Companion.CHANNEL_ID
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import java.io.IOException

class AlarmService : Service() {

    lateinit var mediaPlayer : MediaPlayer
    lateinit var vibrator : Vibrator
    lateinit var alarmData : AlarmData
    lateinit var ringtone : Uri
    lateinit var  sharedPreferenceUtils: SharedPreferenceUtils

    // Service와 Activity 사이의 통신 메서드
    override fun onBind(p0: Intent?): IBinder? {

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

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()

        var alarmTitle: String = alarmData.getTitle

        if (alarmData != null) {
            alarmTitle = alarmData.getTitle
            try {
                mediaPlayer.setDataSource(this.baseContext, Uri.parse(alarmData.getTone))
                mediaPlayer.prepareAsync()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

            val notification : Notification = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Ringing")
                .setContentText(alarmTitle)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent,true)
                .build()

            Log.d("AlarmService : ","mediaPlayer 재생")
            mediaPlayer.setOnPreparedListener { mediaPlayer ->

                mediaPlayer.start()

            }

            val vibratePattern = longArrayOf(1,100,1000,10,100)
            val vibrateAmplitude = intArrayOf(0,20,0,40,0)
            val vibrationEffect = VibrationEffect.createWaveform(vibratePattern,vibrateAmplitude,0)
            vibrator.vibrate(vibrationEffect)
        Log.d("AlarmService : ","startService")

            startForeground(1,notification)
            return START_STICKY

        }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
    }




