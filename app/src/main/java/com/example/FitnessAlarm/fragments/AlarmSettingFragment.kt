
package com.example.FitnessAlarm.fragments
import android.app.Activity
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.FitnessAlarm.CountAlgorithm.PushupCounter
import com.example.FitnessAlarm.CountAlgorithm.SquatCounter
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.activity.MainActivity
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.databinding.AlarmListFragmentBinding
import com.example.FitnessAlarm.databinding.AlarmSettingFragmentBinding

class AlarmSettingFragment : Fragment() {

    lateinit var alarmSettingFragmentBinding : AlarmSettingFragmentBinding

    private lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    private lateinit var tone : String
    private lateinit var ringTone : Ringtone
    private lateinit var ringTitle : String
    private lateinit var alarmData : AlarmData



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        sharedPreferenceUtils = SharedPreferenceUtils(activity?.applicationContext)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()

        // 뷰바인딩
        alarmSettingFragmentBinding = AlarmSettingFragmentBinding.inflate(inflater,container,false)
        val rootView = alarmSettingFragmentBinding.root


        return rootView
    }

    override fun onResume() {
        super.onResume()

        // 알람 정보 가져오기
        loadView(alarmSettingFragmentBinding,alarmData)

        // Ringtone 관련 설정
        tone = alarmData.getTone
        if (tone == "default")
            tone  = RingtoneManager.getActualDefaultRingtoneUri(this.activity?.applicationContext,RingtoneManager.TYPE_ALARM).toString()

        ringTone = RingtoneManager.getRingtone(activity?.applicationContext, Uri.parse(tone.toString()))
        ringTitle = alarmData.getRingTitle

        // timepicker 이벤트
        alarmSettingFragmentBinding.picker.setOnTimeChangedListener{ view, hourOfDay, minute ->
        }

        // spinner에서 운동 종류 고르기
        val workoutSpinner = alarmSettingFragmentBinding.workoutSpinner
        workoutSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                alarmData.workout = workoutSpinner.getItemAtPosition(position) as String

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        // 벨소리 선택 버튼
        alarmSettingFragmentBinding.bellLayout.setOnClickListener {
                view ->
            val intent : Intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,"벨 소리 선택")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,Uri.parse(tone) as Uri)
            startActivityForResult(intent,5)
        }

        // 버튼으로 알람 설정
        alarmSettingFragmentBinding.timeSetBtn.setOnClickListener(){
                view ->
            if (alarmSettingFragmentBinding.repetitionBanner.text.toString().toInt() < 1)
            {
                Toast.makeText(activity,"0 이상의 횟수를 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                //alarmData.cancelAlarm(activity)
                scheduleAlarm()
                Navigation.findNavController(view)
                    .navigate(R.id.action_createAlarmFragment_to_alarmsListFragment)
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == 5){
            val uri : Uri = intent?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)!!
            ringTone = RingtoneManager.getRingtone(activity?.applicationContext,uri)
            ringTitle  = ringTone.getTitle(activity?.applicationContext)
            if (uri != null){
                tone = uri.toString()
                if (ringTitle !=null && !ringTitle.isEmpty())
                    alarmSettingFragmentBinding.musicTitle.setText(ringTitle)
                else{
                    alarmSettingFragmentBinding.musicTitle.setText("")
                }
            }
        }
    }

    // alarmData 정보를 바탕으로 알람을 설정
    private fun scheduleAlarm()
    {
        val alarm : AlarmData = AlarmData(
            1,
            alarmSettingFragmentBinding.picker.hour.toInt(),
            alarmSettingFragmentBinding.picker.minute.toInt(),
            alarmSettingFragmentBinding.workoutSpinner.selectedItem.toString(),
            alarmSettingFragmentBinding.repetitionBanner.text.toString().toInt(),
            true,
            alarmSettingFragmentBinding.monButton.isChecked,
            alarmSettingFragmentBinding.tueButton.isChecked,
            alarmSettingFragmentBinding.wedButton.isChecked,
            alarmSettingFragmentBinding.thuButton.isChecked,
            alarmSettingFragmentBinding.friButton.isChecked,
            alarmSettingFragmentBinding.satButton.isChecked,
            alarmSettingFragmentBinding.sunButton.isChecked,
            alarmSettingFragmentBinding.createAlarmTitle.text.toString(),
            tone,
            ringTitle,
            alarmSettingFragmentBinding.volumeController.progress
        )

        sharedPreferenceUtils.setDataPreference(alarm)
        Log.d("setWorkoutCounter(AlarmSettingFragment)",alarmData.getWorkOut)

        setWorkoutCounter(alarmData.getWorkOut)
        alarm.setAlarm(activity?.applicationContext)
    }
    // 알람 데이터 정보 가져오기
    fun loadView(alarmSettingFragmentBinding: AlarmSettingFragmentBinding,alarmData : AlarmData)
    {
        alarmSettingFragmentBinding.picker.hour = alarmData.getHour
        alarmSettingFragmentBinding.picker.minute = alarmData.getMinute
        loadWorkoutSpinner()
        alarmSettingFragmentBinding.repetitionBanner.setText(alarmData.getRepCnt.toString())
        alarmSettingFragmentBinding.musicTitle.setText(alarmData.getRingTitle)
        alarmSettingFragmentBinding.createAlarmTitle.setText(alarmData.getTitle)
        alarmSettingFragmentBinding.createAlarmTitle.setText(alarmData.getTitle)
        alarmSettingFragmentBinding.volumeController.progress = alarmData.getVolume
    }

    fun loadWorkoutSpinner()
    {
        val adapter = activity?.let{
            ArrayAdapter<String>(it,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.workout_array)
            )
        }

        alarmSettingFragmentBinding.workoutSpinner.adapter = adapter

        if (alarmData.getWorkOut == "스쿼트")
        {
            alarmSettingFragmentBinding.workoutSpinner.setSelection(0)
        }

        else if (alarmData.getWorkOut == "팔굽혀펴기")
        {
            alarmSettingFragmentBinding.workoutSpinner.setSelection(1)
        }
        Log.d("loadWorkoutSpinner",alarmSettingFragmentBinding.workoutSpinner.selectedItem.toString())

    }


    // 설정된 운동 종류에 따라 운동 카운트 알고리즘을 생성
    private fun setWorkoutCounter(workout : String)
    {
        if (workout == "스쿼트") {
            MainActivity.workoutCounter = SquatCounter()
        }

        else if (workout == "팔굽혀펴기") {
            MainActivity.workoutCounter = PushupCounter()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }




}

