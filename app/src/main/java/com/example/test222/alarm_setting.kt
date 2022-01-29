
package com.example.test222
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.core.view.get


class alarm_setting : AppCompatActivity(), TimePicker.OnTimeChangedListener {
    var selectHour : Int? = null
    var selectMin : Int? = null
    var timepicker : TimePicker = findViewById(R.id.picker)
    var time_set_btn : ImageButton = findViewById<ImageButton>(R.id.time_set_btn)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.alarm_detail)

        time_set_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onTimeChanged(timePicker: TimePicker,hour : Int, min: Int) {
        selectHour = hour
        selectMin = min
    }


}