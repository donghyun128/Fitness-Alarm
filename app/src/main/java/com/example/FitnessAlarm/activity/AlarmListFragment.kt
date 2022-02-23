package com.example.FitnessAlarm.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.adapter.AlarmRecycleViewAdapter
import com.example.FitnessAlarm.data.AlarmData
import com.example.FitnessAlarm.data.AlarmDataBase
import com.example.FitnessAlarm.data.AlarmReposit
import com.example.FitnessAlarm.databinding.AlarmListFragmentBinding
import com.example.FitnessAlarm.utils.OnToggleAlarmListener
import com.example.FitnessAlarm.viewModel.AlarmListViewModel
import com.example.FitnessAlarm.viewModel.CreateAlarmViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.NavigationMenu

class AlarmListFragment : Fragment(), OnToggleAlarmListener {

    lateinit var alarmRecyclerViewAdapter : AlarmRecycleViewAdapter
    lateinit var alarmListViewModel : AlarmListViewModel
    lateinit var alarmRecyclerView : RecyclerView
    lateinit var addAlarmButton : FloatingActionButton
    private val mainActivity = activity
    lateinit var alarmListFragmentBinding: AlarmListFragmentBinding
    // 데이터 로드해오기
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        alarmListViewModel = ViewModelProvider(this).get(AlarmListViewModel::class.java)
        alarmListViewModel.getAlarmLiveData().observe(this, Observer
        {
            alarms ->
            fun onChanged(alarm : List<AlarmData>)
            {
                if (alarms != null)
                {
                    alarmRecyclerViewAdapter.setAlarms(alarms)
                }
            }
        }
        )
        alarmRecyclerViewAdapter = AlarmRecycleViewAdapter(requireActivity())

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate the layout
        alarmListFragmentBinding = AlarmListFragmentBinding.inflate(inflater,container,false)

        // fragment View
        val rootView = alarmListFragmentBinding.root

        // recycler View
        alarmRecyclerView = alarmListFragmentBinding.fragmentListalarmsRecylerView
        alarmRecyclerView.layoutManager = LinearLayoutManager(context)
        alarmRecyclerView.adapter = alarmRecyclerViewAdapter

        addAlarmButton = alarmListFragmentBinding.fragmentListAlarmsAddAlarm
        addAlarmButton.setOnClickListener()
        {
            // CreateAlarmFragment로 이동하는 navigation 활용
            it.findNavController().navigate(R.id.action_alarmsListFragment_to_createAlarmFragment)
        }
        alarmRecyclerView = rootView.findViewById<RecyclerView>(R.id.fragment_listalarms_recylerView)

        //val recyclerViewAdapter = AlarmRecyclerViewAdapter(this,)
        //recyclerView.setHasFixedSize(true)

        // 어댑터 생성 후 리사이클러뷰에 어댑터 연결
        //recyclerView.adapter = recyclerViewAdapter
        return rootView
    }
    override fun onToggle(alarm: AlarmData) {
        if (alarm.getOnOff)
        {
        }
        else{


        }
    }

    override fun onDelete(alarm: AlarmData) {
        if (alarm.getOnOff)
        {

        }
        alarmListViewModel.delete(alarm.getAlarmId)
    }

    override fun onitemClick(alarm: AlarmData, view: View) {
        if (alarm.getOnOff)
        {

        }
        var args : Bundle = Bundle()
        args.putParcelable(getString(R.string.arg_alarm_obj),alarm)
        Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment)
    }


}