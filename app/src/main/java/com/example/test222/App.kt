package com.example.test222
import android.app.Application
import com.example.test222.dataSharedPreference

class App : Application(){
    companion object{
        lateinit var prefs : dataSharedPreference
    }

    override fun onCreate() {
        prefs = dataSharedPreference(applicationContext)
        super.onCreate()
    }

}