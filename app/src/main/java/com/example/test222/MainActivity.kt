package com.example.test222

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_detail)

        val set_btn : ImageButton = findViewById(R.id.alarm_set_btn)
        val exe_btn : ImageButton = findViewById(R.id.exercise_set_btn)

        set_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, alarm_setting::class.java)
            startActivity(intent)
        })
    }
}

private fun fetchSharedPreferneces() : alarm_data
{
    val fetchHour : Int = App.prefs.sharedprefernces.getInt("hour",8)
    val fetchMinute : Int = App.prefs.sharedprefernces.getInt("minute",0)
    val fetchOnOff : Boolean = App.prefs.sharedprefernces.getBoolean("onOff",true)
    val alarmInfo : alarm_data = alarm_data(fetchHour,fetchMinute,fetchOnOff)

    return alarmInfo
}
