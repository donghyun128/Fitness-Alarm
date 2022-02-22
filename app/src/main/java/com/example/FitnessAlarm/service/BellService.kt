package com.example.FitnessAlarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BellService : Service() {

    // Service와 Activity 사이의 통신 메서드
    override fun onBind(p0: Intent?): IBinder? {

        return null
    }


    override fun onCreate() {
        super.onCreate()
    }

    // 서비스가 실행될 때 실행
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    // 서비스가 종료될 때 실행행
    override fun onDestroy() {
        super.onDestroy()
    }

}