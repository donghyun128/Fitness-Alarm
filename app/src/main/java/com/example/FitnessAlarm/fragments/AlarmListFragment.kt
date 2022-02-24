package com.example.FitnessAlarm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.databinding.AlarmListFragmentBinding

class AlarmListFragment : Fragment(){

    private lateinit var alarmListFragmentBinding: AlarmListFragmentBinding
    private lateinit var sharedPreferenceUtils : SharedPreferenceUtils
    private lateinit var alarmData : AlarmData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferenceUtils = SharedPreferenceUtils(context)
        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()
        alarmListFragmentBinding = AlarmListFragmentBinding.inflate(inflater,container,false)
        val rootView =  alarmListFragmentBinding.root
        val addAlarmButton = alarmListFragmentBinding.fragmentListAlarmsAddAlarm

        alarmListFragmentBinding.alarmSwitch.isChecked = alarmData.getOnOff

        alarmListFragmentBinding.createAlarmButton.setText("test")
        addAlarmButton.setOnClickListener {
            view ->

                Log.d("Navigation","Navigation")
                Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment)

        }

        val onOffSwitch : Switch = alarmListFragmentBinding.alarmSwitch
        onOffSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == true)
            {
                alarmData.setAlarm(context)
            }
            else
            {
                alarmData.cancelAlarm(context)
            }
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        val alarmData : AlarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()
        alarmListFragmentBinding.createAlarmButton.text = String.format("%s %02d : %02d",alarmData.getAMPM,alarmData.getHour,alarmData.getMinute)

    }
}