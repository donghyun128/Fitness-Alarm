
package com.example.FitnessAlarm.fragments
import android.app.Activity
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.SharedPreferenceUtils
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
        sharedPreferenceUtils = SharedPreferenceUtils(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        alarmData = sharedPreferenceUtils.getAlarmDataFromSharedPreference()
        alarmSettingFragmentBinding = AlarmSettingFragmentBinding.inflate(inflater,container,false)
        val rootView = alarmSettingFragmentBinding.root

        alarmSettingFragmentBinding.repetitionBanner.setText(alarmData.getRepCnt.toString())
        alarmSettingFragmentBinding.musicTitle.setText(alarmData.getRingTitle)
        alarmSettingFragmentBinding.createAlarmTitle.setText(alarmData.getTitle)
        //loadView(alarmSettingFragmentBinding,alarmData)

        // Ringtone 관련 설정
        tone  = RingtoneManager.getActualDefaultRingtoneUri(this.context,RingtoneManager.TYPE_ALARM).toString()
        ringTone = RingtoneManager.getRingtone(context, Uri.parse(tone.toString()))

        // timepicker 이벤트
        alarmSettingFragmentBinding.picker.setOnTimeChangedListener{ view, hourOfDay, minute ->

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
            scheduleAlarm()
            Navigation.findNavController(view).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment)
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == 5){
            val uri : Uri = intent?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)!!
            ringTone = RingtoneManager.getRingtone(context,uri)
            var title : String = ringTone.getTitle(context)
            if (uri != null){
                tone = uri.toString()
                if (title !=null && !title.isEmpty())
                    alarmSettingFragmentBinding.musicTitle.setText(title)
                else{
                    alarmSettingFragmentBinding.musicTitle.setText("")
                }
            }
        }
    }

    private fun scheduleAlarm()
    {
        val alarm : AlarmData = AlarmData(
            0,
            alarmSettingFragmentBinding.picker.hour.toInt(),
            alarmSettingFragmentBinding.picker.minute.toInt(),
            alarmSettingFragmentBinding.workoutSpinner.toString(),
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
            ringTone.getTitle(context)
        )

        sharedPreferenceUtils.setDataPreference(alarm)
        alarm.setAlarm(context)
    }

    fun loadView(alarmSettingFragmentBinding: AlarmSettingFragmentBinding,alarmData : AlarmData)
    {
        alarmSettingFragmentBinding.repetitionBanner.setText(alarmData.getRepCnt)
        alarmSettingFragmentBinding.musicTitle.setText(alarmData.getRingTitle)
        alarmSettingFragmentBinding.createAlarmTitle.setText(alarmData.getTitle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }




}

