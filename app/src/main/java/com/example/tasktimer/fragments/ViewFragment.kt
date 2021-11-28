package com.example.tasktimer.fragments

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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


class ViewFragment : Fragment() {

    lateinit var adapter: HomeRecyclerView
    lateinit var mainTitle: TextView
    lateinit var mainTime: TextView
    lateinit var mainDescription: TextView
    var tasks = listOf<Task>()
    val taskViewModel by lazy { TaskViewModel(requireActivity().application) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_view, container, false)

        taskViewModel.deactivateAllTasks()

        mainTitle = view.findViewById(R.id.tvTotalmain)
        mainDescription = view.findViewById(R.id.tvDescriptionmain)
        mainTime = view.findViewById(R.id.tvTimemain)

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


}