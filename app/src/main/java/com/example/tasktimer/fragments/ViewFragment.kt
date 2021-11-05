package com.example.tasktimer.fragments

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.HomeRecyclerView
import com.example.tasktimer.R
import androidx.core.view.isVisible
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_view.*
import kotlinx.android.synthetic.main.item_row.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mainTitle: TextView
    lateinit var mainTime: TextView
    lateinit var pauseButton: Button
    lateinit var restartButton: Button
    var tasks = listOf<Task>()
    var isFirstTime = true
    val taskViewModel by lazy { TaskViewModel(requireActivity().application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view, container, false)

        mainTitle = view.findViewById(R.id.tvTotalmain)
        mainTime = view.findViewById(R.id.tvTimemain)
        pauseButton = view.findViewById(R.id.btnPause)
        restartButton = view.findViewById(R.id.btnRestart)
        showingButtons(false)

        val rvMain = view.findViewById<RecyclerView>(R.id.rvMain)
        val adapter = HomeRecyclerView(requireActivity().application, this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.getAllTasks().observe(viewLifecycleOwner, {
                allTasks -> kotlin.run {
                    adapter.update(allTasks)
                    tasks = allTasks
                }
        })

        return view
    }

    fun showingButtons(state: Boolean){
        pauseButton.isVisible = state
        restartButton.isVisible = state
    }

    fun stopAllOtherTimers(tasks: List<Task>, id:Int){
        for (i in tasks.indices){
            if(tasks[i].id != id && tasks[i].active){
                rvMain.findViewHolderForAdapterPosition(i)!!.itemView.chronometer.stop()
                tasks[i].pauseOffset = SystemClock.elapsedRealtime() -
                        rvMain.findViewHolderForAdapterPosition(i)!!.itemView.chronometer.base
                tasks[i].active = false
                taskViewModel.updateTask(tasks[i])
            }
        }
    }


    override fun onStop() {
        super.onStop()
        for (i in tasks.indices){
            tasks[i].timer = rvMain.findViewHolderForAdapterPosition(i)!!.itemView.chronometer.text.toString()
            Log.e("TAG","${tasks[i].timer}")
            isFirstTime = true
            if(tasks[i].active){
                tasks[i].pauseOffset = SystemClock.elapsedRealtime() -
                        rvMain.findViewHolderForAdapterPosition(i)!!.itemView.chronometer.base
            }
            taskViewModel.updateTask(tasks[i])
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        taskViewModel.deactivateAllTasks()
        Log.e("TAG","destroy")
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                ViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}