package com.example.FitnessAlarm.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.activity.MainActivity.Companion.CHANNEL_ID
import com.example.FitnessAlarm.data.AlarmData
import java.io.IOException

class AlarmService : Service() {

    lateinit var mediaPlayer : MediaPlayer
    lateinit var vibrator : Vibrator
    lateinit var alarmData : AlarmData
    lateinit var ringtone : Uri

    // Service와 Activity 사이의 통신 메서드
    override fun onBind(p0: Intent?): IBinder? {

        return null
    }


    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = true
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.baseContext,RingtoneManager.TYPE_ALARM)

    }

    // 서비스가 실행될 때 실행
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        val bundle : Bundle? = intent?.getBundleExtra(getString(R.string.arg_alarm_obj))
        if (bundle != null){
            alarmData = bundle.getParcelable<AlarmData>(getString(R.string.arg_alarm_obj))!!
        }

        val notificationIntent : Intent = Intent(this,CameraActivity::class.java)
        notificationIntent.putExtra(getString(R.string.bundle_alarm_obj),bundle)

        val pendingIntent : PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        var alarmTitle : String = getString(R.string.alarm_title)

        if (alarmData !=null)
        {
            alarmTitle = alarmData.getTitle
           try {
               mediaPlayer.setDataSource(this.baseContext,Uri.parse(alarmData.getTone))
               mediaPlayer.prepareAsync()
           } catch (ex : IOException){
               ex.printStackTrace()
           }
        }

        else{
            try {
                mediaPlayer.setDataSource(this.baseContext,ringtone)
                mediaPlayer.prepareAsync()
            }catch (ex : IOException){
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

        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            mediaPlayer ->
            {
                mediaPlayer.start()
            }
        })

        val vibratePattern = longArrayOf(1,100,1000)
        val vibrateAmplitude = intArrayOf(0,20,0,40,0)
        val vibrationEffect = VibrationEffect.createWaveform(vibratePattern,vibrateAmplitude,0)
        vibrator.vibrate(vibrationEffect)

        startForeground(1,notification)

        return START_STICKY

    }

    // 서비스가 종료될 때 실행행
    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.stop()
        vibrator.cancel()
    }

}