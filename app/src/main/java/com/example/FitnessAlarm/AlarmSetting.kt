
package com.example.FitnessAlarm
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.FitnessAlarm.MainActivity.Companion
import com.example.FitnessAlarm.MainActivity.Companion.HOUR_KEY
import com.example.FitnessAlarm.MainActivity.Companion.MINUTE_KEY
import com.example.FitnessAlarm.MainActivity.Companion.REPETITION_KEY
import com.example.FitnessAlarm.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.FitnessAlarm.MainActivity.Companion.WORKOUT_KEY
import com.example.FitnessAlarm.data.AlarmData
import java.io.File

class AlarmSetting : AppCompatActivity() {

    var selectHour : String? = null
    var selectMin : String? = null
    var alarmMusic : Uri? = null
    var selectedFileTitle : String? = ""
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result ->
        run{

            if (result.resultCode == RESULT_OK)
            {
                alarmMusic = result.data!!.data
                //selectedFileTitle = result.data!!.dataString
            }
        }
    }

    // TimePicker를 통해 Alarm 시간 변경
    private fun changeAlarmTime()
    {
        val timepicker : TimePicker = findViewById(R.id.picker)
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)

        // Alarm Setting 화면 실행 시, 기존의 알람 시간을 timepicker에 표시
        timepicker.setHour(sharedPreference.getString(HOUR_KEY,"8")!!.toInt())
        timepicker.setMinute(sharedPreference.getString(MINUTE_KEY,"0")!!.toInt())


        // timepicker 설정 변경 시, 알람 시간 변경
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
    private fun changeAlarmWorkout(context : Context)
    {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val workout_item = resources.getStringArray(R.array.workout_array)
        val current_workout = sharedPreference.getString(WORKOUT_KEY,"squat")

        val workout_spinner : Spinner = findViewById(R.id.workout_spinner)
        val workout_adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,workout_item)
        val time_set_btn : Button = findViewById<Button>(R.id.time_set_btn)


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
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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

    private fun changeWorkOutRepetition(context: Context)
    {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val repetition_text : EditText = findViewById(R.id.repetition_banner)
        val current_rep = sharedPreference.getInt(REPETITION_KEY,2)
        MainActivity.workoutCounter.completeGoal = current_rep
        Log.i("rep test","sharedPreferenceREP : " + current_rep.toString())
        Log.i("rep test","workoutCounter.completeGoal : " + MainActivity.workoutCounter.completeGoal.toString())

        repetition_text.setText(current_rep.toString())

        var rep_num : Int = 0
        repetition_text.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                try {
                    rep_num = s.toString().toInt()
                    sharedPreference.edit().putInt(REPETITION_KEY,rep_num).apply()
                    MainActivity.workoutCounter.completeGoal = rep_num
                    Log.i("repetition goal","repetition goal" + MainActivity.workoutCounter.completeGoal.toString())
                } catch (e : NumberFormatException) {
                    Toast.makeText(context,"숫자만 입력하세요",Toast.LENGTH_SHORT).show()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                try{

                } catch (e : java.lang.NumberFormatException){
                    Toast.makeText(context,"다시 입력해주세요",Toast.LENGTH_SHORT).show()
                }
            }
        }
        )
    }

    // 벨소리 레이아웃 부분을 클릭하면 음악 변경 가능
    private fun changeAlarmMusic()
    {
        val bellLayout : ViewGroup = findViewById(R.id.bell_layout)
        bellLayout.setOnClickListener()
        {

            val alarmTitle : TextView = findViewById(R.id.musicTitle)

            // 파일탐색기로 음악 불러오기
            val musicIntent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            musicIntent.setType("audio/*")
            musicIntent.putExtra("Title",2)

            Log.d("AlarmSetting","resultLauncher.launch()")
            resultLauncher.launch(musicIntent)
            if (alarmMusic != null) {
                Log.d("AlarmSetting","setText")
                selectedFileTitle = File(alarmMusic!!.path).absolutePath
                alarmTitle.setText(selectedFileTitle)
            }
            //resultLauncher.launch("audio/*")

            //startActivityForResult(musicIntent,1)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_setting)

    }

    override fun onResume() {
        super.onResume()

        changeAlarmTime()
        changeAlarmWorkout(this)
        changeWorkOutRepetition(this)
        changeAlarmMusic()

    }




    private fun saveAlarmData(alarmId : Int, hour : String, min : String, workout : String,
                              repCnt : Int , onOff : Boolean,
                              monday : Boolean, tuesday : Boolean, wednesday : Boolean, thursday : Boolean, friday : Boolean, saturday : Boolean, sunday : Boolean,
                              title : String, tone : String) : AlarmData
    {
        val sharedPreferences = getSharedPreferences(Companion.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val DataModel = AlarmData(
            alarmId = alarmId,
            hour = hour,
            min = min,
            workout = workout,
            repCnt = repCnt,
            onOff = onOff,
            monday = monday,
            tuesday = tuesday,
            wednesday = wednesday,
            thursday = thursday,
            friday = friday,
            saturday = saturday,
            sunday = sunday,
            title = title,
            tone = tone
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

