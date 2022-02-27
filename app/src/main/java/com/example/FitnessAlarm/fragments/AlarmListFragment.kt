package com.example.FitnessAlarm.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
        // 뷰바인딩
        alarmListFragmentBinding = AlarmListFragmentBinding.inflate(inflater,container,false)
        val rootView =  alarmListFragmentBinding.root
        return rootView
    }

    override fun onResume() {
        super.onResume()

        Log.d("ListFragment","onCreateView")
        sharedPreferenceUtils = SharedPreferenceUtils(activity?.applicationContext)
        Log.d("ListFragment","getAlarmData")
        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()



        // 알람 시각
        alarmListFragmentBinding.timeText.setText(alarmData.timeToText)

        // 알람 예정 운동 횟수 보여주기
        if (alarmData.getRepCnt == 0)
        {
            alarmListFragmentBinding.repetitionText.setText(" ")
        }
        else
        {
            var repText : String = getString(R.string.repetition_notify,alarmData.getWorkOut,alarmData.getRepCnt)
            Log.d("repetitionText String",repText)
            alarmListFragmentBinding.repetitionText.setText(repText)
        }

        // 알람 설정 버튼 이벤트
        val addAlarmButton = alarmListFragmentBinding.fragmentListAlarmsAddAlarm
        addAlarmButton.setOnClickListener {
                view ->

            Log.d("Navigation","Navigation")
            Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment)

        }

        // 알람 on/off 토글버튼
        Log.d("ListFragment","set alarmSwitch Onoff")
        Log.d("ListFragment",alarmData.getOnOff.toString())
        val onOffSwitch : ToggleButton = alarmListFragmentBinding.alarmSwitch
        if(onOffSwitch.isChecked != alarmData.onOff)
            onOffSwitch.toggle()
        Log.d("onCreateView",onOffSwitch.isChecked.toString() + "/ " + alarmData.getOnOff)

        onOffSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                alarmData.setAlarm(activity?.applicationContext)
                sharedPreferenceUtils.setDataPreference(alarmData)
            }
            else
            {
                alarmData.cancelAlarm(activity?.applicationContext)
                sharedPreferenceUtils.setDataPreference(alarmData)
            }
        }

    }


    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 99)
    }
}