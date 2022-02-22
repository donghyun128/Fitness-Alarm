package com.example.FitnessAlarm.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData

class CreateAlarmFragment : Fragment() {


    private lateinit var tone : String
    private lateinit var alarmData : AlarmData

    private lateinit var timepicker : TimePicker
    private lateinit var workoutTitle : TextView
    private lateinit var ringText : TextView
    private lateinit var ringTitle : TextView
    private lateinit var spinner : Spinner
    private lateinit var workoutEditText: EditText
    //val workout_item = resources.getStringArray(R.array.workout_array)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null){

             //alarmData =arguments.getParcelable<AlarmData>(R.string.arg_alarm_obj)


        }


    }
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_alarm_fragment,container,false)
        timepicker = view.findViewById<TimePicker>(R.id.picker)
        workoutTitle = view.findViewById(R.id.workout_banner)
        spinner = view.findViewById(R.id.workout_spinner)
        workoutEditText = view.findViewById(R.id.repetition_banner)
        ringTitle = view.findViewById(R.id.musicTitle)

        return inflater.inflate(R.layout.alarm_list_fragment,container,false)
    }
}