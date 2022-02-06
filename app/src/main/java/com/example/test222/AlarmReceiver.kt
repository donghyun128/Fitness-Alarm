package com.example.test222

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmReceiver : BroadcastReceiver() {
    companion object
    {
        const val NOTIFICATION_CHANNEL_ID = "222"
        const val NOTIFICATION_ID = 222
    }

    override fun onReceive(context: Context, intent: Intent) {

    }

}