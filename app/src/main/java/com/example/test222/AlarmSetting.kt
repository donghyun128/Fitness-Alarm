
package com.example.test222
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.test222.MainActivity.Companion
import com.example.test222.MainActivity.Companion.HOUR_KEY
import com.example.test222.MainActivity.Companion.MINUTE_KEY
import com.example.test222.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.test222.MainActivity.Companion.WORKOUT_KEY

class AlarmSetting : AppCompatActivity() {

    var selectHour : String? = null
    var selectMin : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_setting)


        changeAlarmTime()
        changeAlarmWorkout()

    }

    // TimePicker를 통해 Alarm 시간 변경
    private fun changeAlarmTime()
    {
        val timepicker : TimePicker = findViewById(R.id.picker)
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        timepicker.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener
        {
            override fun onTimeChanged(timepicker: TimePicker,hour : Int, min: Int) {


                sharedPreference.edit().putString(HOUR_KEY,hour.toString()).apply()
                sharedPreference.edit().putString(MINUTE_KEY,min.toString()).apply()

                selectHour = sharedPreference.getString(HOUR_KEY,"8")!!
                selectMin = sharedPreference.getString(MINUTE_KEY,"00")!!

            }


        }
        )


    }

    // Alarm Workout 종류를 변경
    private fun changeAlarmWorkout()
    {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)
        val workout_item = resources.getStringArray(R.array.workout_array)
        val workout_spinner : Spinner = findViewById(R.id.workout_spinner)
        val workout_adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,workout_item)
        val time_set_btn : ImageButton = findViewById<ImageButton>(R.id.time_set_btn)
        val current_workout = sharedPreference.getString(WORKOUT_KEY,"squat")

        workout_spinner.adapter = workout_adapter
        when (current_workout)
        {
            "squat" -> { workout_spinner.setSelection(0)}
            "pushup" -> { workout_spinner.setSelection(1) }
            else -> {workout_spinner.setSelection(0)}
        }

        time_set_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })

        workout_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position){

                    0 -> {
                        sharedPreference.edit().putString(WORKOUT_KEY,"squat").apply()
                    }

                    1 -> {
                        sharedPreference.edit().putString(WORKOUT_KEY,"pushup").apply()
                    }

                    else -> {
                        sharedPreference.edit().putString(WORKOUT_KEY,"squat").apply()
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun saveAlarmData(hour : String, min : String, workout : String, onOff : Boolean) : AlarmData
    {
        val sharedPreferences = getSharedPreferences(Companion.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val DataModel = AlarmData(
            hour = hour,
            min = min,
            workout = workout,
            onOff = onOff
        )

        // 키와 값 전달
        with(sharedPreferences.edit())
        {
            putString(HOUR_KEY,DataModel.getHour)
            putString(MINUTE_KEY,DataModel.getMinute)
            putString(WORKOUT_KEY,DataModel.getWorkOut)
            putBoolean(Companion.ONOFF_KEY,DataModel.getOnOff)
            commit()
        }

        return DataModel

    }




}

