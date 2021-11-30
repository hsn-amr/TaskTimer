package com.example.tasktimer.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.HomeRecyclerView
import com.example.tasktimer.R
import androidx.core.view.isVisible
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_view.*
import kotlinx.android.synthetic.main.item_row.view.*


class ViewFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter: HomeRecyclerView
    lateinit var mainTitle: TextView
    lateinit var mainTimer: Chronometer
    lateinit var mainDescription: TextView
    private var tasks = listOf<Task>()
    private val taskViewModel by lazy { TaskViewModel(requireActivity().application) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_view, container, false)

        taskViewModel.deactivateAllTasks()

        mainTitle = view.findViewById(R.id.tvTotalmain)
        mainDescription = view.findViewById(R.id.tvDescriptionmain)
        mainTimer = view.findViewById(R.id.tvTimemain)

        val rvMain = view.findViewById<RecyclerView>(R.id.rvMain)
        adapter = HomeRecyclerView(requireActivity().application, this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(requireContext()).apply {
            recycleChildrenOnDetach = false
        }

        taskViewModel.getAllTasks().observe(viewLifecycleOwner, {
                allTasks -> kotlin.run {
                    adapter.update(allTasks)
                    tasks = allTasks
                }
        })

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

}