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

class AlarmListFragment : Fragment(), OnToggleAlarmListener {

    lateinit var alarmRecyclerViewAdapter : AlarmRecycleViewAdapter
    lateinit var alarmListViewModel : AlarmListViewModel
    lateinit var alarmRecyclerView : RecyclerView
    lateinit var addAlarmButton : FloatingActionButton

    // 데이터 로드해오기
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        alarmListViewModel = ViewModelProvider(this).get(AlarmListViewModel::class.java)
        alarmListViewModel.getAlarmLiveData().observe(this, Observer
        {
            fun onChanged(it : List<AlarmData>)
            {
                if (it != null)
                {
                    alarmRecyclerViewAdapter.setAlarms(it)
                }
            }
        }
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // fragment View
        val rootView = inflater.inflate(R.layout.alarm_list_fragment,container,false)
        // recycler View
        alarmRecyclerView = rootView.findViewById<RecyclerView>(R.id.fragment_listalarms_recylerView)
        //val recyclerViewAdapter = AlarmRecyclerViewAdapter(this,)
        //recyclerView.setHasFixedSize(true)

        // 어댑터 생성 후 리사이클러뷰에 어댑터 연결
        //recyclerView.adapter = recyclerViewAdapter
        return rootView
    }
    override fun onToggle(alarm: AlarmData) {
    }

    override fun onDelete(alarm: AlarmData) {
    }

    override fun onitemClick(alarm: AlarmData, view: View) {
    }
}