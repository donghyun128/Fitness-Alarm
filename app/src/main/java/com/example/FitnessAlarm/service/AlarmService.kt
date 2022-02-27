package com.example.FitnessAlarm.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.FitnessAlarm.App.Companion.CHANNEL_ID
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.CameraActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import java.io.IOException
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


        val pendingIntent = createNotificationIntent()

        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()

        // 벨소리 정보 load
        var alarmTitle: String = alarmData.getTitle
        val audioAttributes : AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        if (alarmData != null) {
            alarmTitle = alarmData.getTitle
            try {
                mediaPlayer.setDataSource(this.baseContext, Uri.parse(alarmData.getTone))
                mediaPlayer.setVolume(alarmData.volume.toFloat(),alarmData.volume.toFloat())
                mediaPlayer.setAudioAttributes(audioAttributes)
                mediaPlayer.prepareAsync()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        val notification = createNotification(pendingIntent)
        val foreGroundNotification = createForegroundNotification(pendingIntent)

        mediaPlayer.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }

        // 진동패턴 설정
        val vibratePattern = longArrayOf(1,100,1000,10,100)
        val vibrateAmplitude = intArrayOf(100,85,100,80,100)
        val vibrationEffect = VibrationEffect.createWaveform(vibratePattern,vibrateAmplitude,0)
        vibrator.vibrate(vibrationEffect)

        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startForeground(1,foreGroundNotification)
        notificationManager.notify(1,notification)

        return START_REDELIVER_INTENT

        }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }

    fun createNotification(pendingIntent : PendingIntent) : Notification
    {
        val contextText : String = getString(R.string.repetition_notify,alarmData.getWorkOut,alarmData.getRepCnt)
        val largeIcon = BitmapFactory.decodeResource(resources,R.drawable.app_icon)

        //notification 설정
        val notification : Notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(alarmData.timeToText)
            .setContentText(contextText)
            .setSound(null)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent,true)
            .setSmallIcon(R.drawable.ic_alarm_white_24dp)
            .setLargeIcon(largeIcon)
            .setOngoing(true)
            .build()

        return notification
    }

    fun createForegroundNotification(pendingIntent : PendingIntent) : Notification
    {
        val remoteViews : RemoteViews = RemoteViews(packageName,R.layout.notication_0dp)
        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setCustomContentView(remoteViews)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .build()

        return notification
    }

    fun createNotificationIntent() : PendingIntent
    {
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

        // notification PendingIntent 생성
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            0
        )

        return pendingIntent

    }

}




