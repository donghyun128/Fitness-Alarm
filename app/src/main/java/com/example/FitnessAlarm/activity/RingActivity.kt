package com.example.FitnessAlarm.activity

import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.service.AlarmService

class RingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring)

        val btn : Button = findViewById(R.id.start_camera_button)
        btn.setOnClickListener{
            view ->
            val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

    }



}