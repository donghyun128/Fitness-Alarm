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

        // 뷰바인딩
        alarmListFragmentBinding = AlarmListFragmentBinding.inflate(inflater,container,false)
        val rootView =  alarmListFragmentBinding.root

        // 알람 시각
        alarmListFragmentBinding.timeText.setText(alarmData.timeToText)

        // 알람 운동 횟수 보여주기
        if (alarmData.getRepCnt == 0)
        {
            alarmListFragmentBinding.repetitionText.setText(" ")
        }
        else
        {
            //var repText : String = getString(R.string.repetition_notify,alarmData.getWorkOut,alarmData.getRepCnt)
            alarmListFragmentBinding.repetitionText.setText("스쿼트 " + alarmData.repCnt.toString() + "회 예정")
        }

        // 알람 설정 버튼 이벤트
        val addAlarmButton = alarmListFragmentBinding.fragmentListAlarmsAddAlarm
        addAlarmButton.setOnClickListener {
            view ->

                Log.d("Navigation","Navigation")
                Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment)

        }

        // 알람 on/off 토글버튼
        alarmListFragmentBinding.alarmSwitch.isChecked = alarmData.getOnOff
        val onOffSwitch : ToggleButton = alarmListFragmentBinding.alarmSwitch
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

    }

    //권한 확인
    fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), 99)
    }
}