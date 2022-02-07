package com.example.test222

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import android.content.SharedPreferences
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.test222.data.dataSharedPreference
import com.example.test222.MainActivity

class AlarmReceiver : BroadcastReceiver() {


    companion object
    {
        const val NOTIFICATION_CHANNEL_ID = "222"
        const val NOTIFICATION_ID = 222
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i("test","test activity")

        createNotificationChannel(context)
        notifyNotification(context)


        //val alarmIntent = Intent(context,Counter::class.java)
        //context.startActivity(alarmIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    // 채널 생성
    private fun createNotificationChannel(context : Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationchannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "알람",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationIntent : Intent = Intent(context,Counter::class.java)
            val notificationPendingIntent : PendingIntent = PendingIntent.getActivity(context,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            NotificationManagerCompat.from(context)
                .createNotificationChannel(notificationchannel)

            val notifyLayout : RemoteViews = RemoteViews(context.packageName,R.layout.notification)
            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)

            // 커스텀 레이아웃 적용
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            builder.setCustomContentView(notifyLayout)
            // notification 누를 시 Intent 이동
            builder.setContentIntent(notificationPendingIntent)
            builder.setAutoCancel(true)

            val notificationManagerCompat : NotificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(NOTIFICATION_ID,builder.build())

        }

    }
    // 알림
    private fun notifyNotification(context : Context)
    {
        val pref : SharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText(pref.getString(MainActivity.WORKOUT_KEY,"운동"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            notify(NOTIFICATION_ID, build.build())
        }
    }



}