package com.example.tasktimer.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.HomeRecyclerView
import com.example.tasktimer.R
import com.example.tasktimer.interfaces.ActiveTask
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel


class ViewFragment : Fragment(), ActiveTask {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rvMain: RecyclerView
    private lateinit var adapter: HomeRecyclerView
    private lateinit var mainTitle: TextView
    private lateinit var mainTimer: Chronometer
    private lateinit var mainDescription: TextView
    private var tasks = listOf<Task>()
    private val taskViewModel by lazy { TaskViewModel(requireActivity().application) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_view, container, false)

        mainTitle = view.findViewById(R.id.tvTotalmain)
        mainDescription = view.findViewById(R.id.tvDescriptionmain)
        mainTimer = view.findViewById(R.id.tvTimemain)

        rvMain = view.findViewById(R.id.rvMain)
        adapter = HomeRecyclerView(this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(requireContext()).apply {
            recycleChildrenOnDetach = false
        }

        taskViewModel.getAllTasks().observe(viewLifecycleOwner) { allTasks ->
            kotlin.run {
                adapter.update(allTasks)
                tasks = allTasks
            }
        }

        return view
    }


    override fun onStop() {
        super.onStop()
        // stop active timer before user moves to other fragment
        if(didUserMove()){
            stopActiveTimer()
            updateMoveState()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // stop active timer before app close
        stopActiveTimer()
    }



    private fun didUserMove(): Boolean{
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), AppCompatActivity.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean("userMoved", false)
    }

    private fun updateMoveState(){
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), AppCompatActivity.MODE_PRIVATE
        )
        with(sharedPreferences.edit()){
            putBoolean("userMoved", false)
            apply()
        }
    }

    private fun stopActiveTimer(){
        for (task in tasks){
            if(task.active){
                Toast.makeText(requireContext(), "Timer Paused", Toast.LENGTH_LONG).show()
                task.timer = mainTimer.text.toString()
                mainTimer.stop()
                task.pauseOffset = SystemClock.elapsedRealtime() - mainTimer.base
                task.active = false
                task.isClicked = false
                taskViewModel.updateTask(task)
            }
        }
    }

    private fun getTotalFromString(oldString: String, newString: String): String {
        var oldHours = 0
        val oldMinutes: Int
        val oldSeconds: Int
        val oldStringArray = oldString.split(":")
        if (oldStringArray.size == 2) {
            oldMinutes = oldStringArray[0].toInt()
            oldSeconds = oldStringArray[1].toInt()
        } else {
            oldHours = oldStringArray[0].toInt()
            oldMinutes = oldStringArray[1].toInt()
            oldSeconds = oldStringArray[2].toInt()
        }

        var newHours = 0
        var newMinutes: Int
        var newSeconds: Int
        val newStringArray = newString.split(":")
        if (newStringArray.size == 2) {
            newMinutes = newStringArray[0].toInt() + oldMinutes
            newSeconds = newStringArray[1].toInt() + oldSeconds
        } else {
            newHours = newStringArray[0].toInt() + oldHours
            newMinutes = newStringArray[1].toInt() + oldMinutes
            newSeconds = newStringArray[2].toInt() + oldSeconds
        }

        if (newSeconds >= 60) {
            newMinutes += newSeconds / 60
            newSeconds %= 60
        }
        if (newMinutes >= 60) {
            newHours += newMinutes / 60
            newMinutes %= 60
        }

        return (if (newHours < 10) "0$newHours" else newHours).toString() +
                ":" + (if (newMinutes < 10) "0$newMinutes" else newMinutes) +
                ":" + if (newSeconds < 10) "0$newSeconds" else newSeconds
    }

    override fun setTitle(title: String) {
        this.mainTitle.text = title
    }

    override fun setDescription(description: String) {
        this.mainDescription.text = description
    }

    override fun startTimer(task: Task) {
        setTitle(task.task)
        setDescription(task.description)
        this.mainTimer.base = SystemClock.elapsedRealtime() - task.pauseOffset
        this.mainTimer.start()

        task.isClicked = false
        taskViewModel.updateTask(task)
    }

    override fun stopTimer(task: Task) {
        task.timer = this.mainTimer.text.toString()
        this.mainTimer.stop()
        task.pauseOffset = SystemClock.elapsedRealtime() - this.mainTimer.base
        task.active = false
        task.isClicked = false
        taskViewModel.updateTask(task)
    }

    override fun restartTimer(task: Task) {
        Log.e("TAG", "${task.totalTime} - ${this.mainTimer.text}")
        task.totalTime = getTotalFromString(task.totalTime, this.mainTimer.text.toString())
        this.mainTimer.base = SystemClock.elapsedRealtime()
        this.mainTimer.stop()
        task.timer = "00:00"
        task.pauseOffset = 0
        task.active = false
        task.isClicked = false

        taskViewModel.updateTask(task)
        this.mainTitle.text = ""
        this.mainDescription.text = ""
    }

    override fun onTimerClick(task: Task) {
        task.active = true
        task.isClicked = true
        taskViewModel.updateTask(task)
    }

    override fun onTimerTickListener(callBack: (time: String) -> Unit) {
        this.mainTimer.onChronometerTickListener = Chronometer.OnChronometerTickListener { mainChronometer ->
            callBack(mainChronometer.text.toString())
        }
    }

}