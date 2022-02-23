package com.example.FitnessAlarm.activity

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.databinding.CreateAlarmFragmentBinding
import com.example.FitnessAlarm.viewModel.CreateAlarmViewModel

class CreateAlarmFragment : Fragment() {


    private lateinit var createAlarmFragmentBinding: CreateAlarmFragmentBinding
    private lateinit var tone : Uri
    private lateinit var ringTone : Ringtone

    private lateinit var alarmData : AlarmData

    private lateinit var timepicker : TimePicker
    private lateinit var workoutTitle : TextView
    private lateinit var ringText : TextView
    private lateinit var ringTitle : TextView
    private lateinit var spinner : Spinner
    private lateinit var workoutEditText: EditText
    private lateinit var createAlarmViewModel: CreateAlarmViewModel
    //val workout_item = resources.getStringArray(R.array.workout_array)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null){
            val bundle = requireArguments()
            alarmData = bundle.getParcelable<AlarmData>(R.string.arg_alarm_obj.toString())!!
        }
        createAlarmViewModel = ViewModelProvider(this).get(CreateAlarmViewModel::class.java)

    }
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        createAlarmFragmentBinding = CreateAlarmFragmentBinding.inflate(inflater,container,false)
        val rootView = createAlarmFragmentBinding.root

        tone  = RingtoneManager.getActualDefaultRingtoneUri(this.context,RingtoneManager.TYPE_ALARM)
        ringTone = RingtoneManager.getRingtone(context, Uri.parse(tone.toString()))

        /*
        timepicker = createAlarmFragmentBinding.
        workoutTitle = view.findViewById(R.id.workout_banner)
        spinner = view.findViewById(R.id.workout_spinner)
        workoutEditText = view.findViewById(R.id.repetition_banner)
        ringTitle = view.findViewById(R.id.musicTitle)
        */


        // 음악 설정 버튼(레이아웃)
        createAlarmFragmentBinding.bellLayout.setOnClickListener(){
        }

        // 버튼으로 알람 설정
        createAlarmFragmentBinding.timeSetBtn.setOnClickListener(){

        }

        return rootView



    }
}