package com.example.test222

import android.app.*
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
import android.media.Ringtone
import android.media.RingtoneManager
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

        //

        val ringtone = createRingtone(context)

        ringtone.play()

        executeNotification(context)

        ringtone.stop()


    }

    // pendingIntent 생성
    private fun createPendingIntent(context : Context) : PendingIntent?
    {
        val counterIntent = Intent(context,Counter::class.java)

        val counterPendingIntent : PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(counterIntent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        return counterPendingIntent
    }


    // Head up notification 채널, 빌더 생성 및 notification 실행
    private fun executeNotification(context : Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // notification channel 생성
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "알람",
                NotificationManager.IMPORTANCE_HIGH)

            // 커스텀 레이아웃 불러오기
            val remoteViews : RemoteViews = RemoteViews(context.packageName, R.layout.notification)



            // notification builder 생성 및 설정
            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigPictureStyle())
                .setOngoing(true) // notification을 슬라이드해서 종료할 수 없음.
                .setAutoCancel(true)
                .setCustomHeadsUpContentView(remoteViews) // notification에 커스텀 레이아웃, pendingIntent 장착
                .setFullScreenIntent(createPendingIntent(context),true) // Head up Notification

            // notification 실행
            val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID,builder.build())


        }
    }

    private fun createRingtone(context :Context) : Ringtone
    {
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val ringtone = RingtoneManager.getRingtone(context,ringtoneUri)
        return ringtone
    }
}

