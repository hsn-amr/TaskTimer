package com.example.tasktimer.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.R
import com.example.tasktimer.TotalTimeRecyclerView
import com.example.tasktimer.interfaces.Dialogs
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel

class TotalTimeFragment : Fragment(), Dialogs {

    private val taskViewModel by lazy { TaskViewModel(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_total_time, container, false)

        val taskViewModel by lazy { TaskViewModel(requireActivity().application) }

        val rvMain = view.findViewById<RecyclerView>(R.id.rvMain)
        val adapter = TotalTimeRecyclerView(this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.getAllTasks().observe(viewLifecycleOwner) { allTasks ->
            adapter.update(allTasks)
        }

        return view
    }

    override fun showUpdateTaskAlterDialog(task: Task) {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL

        val taskTitle = EditText(requireContext())
        val taskDescription = EditText(requireContext())
        taskTitle.setText(task.task)
        taskDescription.setText(task.description)

        layout.addView(taskTitle)
        layout.addView(taskDescription)

        AlertDialog.Builder(requireContext())
            .setTitle("Update Task")
            .setView(layout)
            .setPositiveButton("Update"){_,_ ->
                task.task = taskTitle.text.toString()
                task.description = taskDescription.text.toString()

                taskViewModel.updateTask(task)
            }
            .setNegativeButton("Cancel"){dialogFace,_ -> dialogFace.cancel()}
            .create()
            .show()
    }

    override fun showDeleteTaskAlertDialog(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Do You Want To Delete This Task?")
            .setPositiveButton("yes"){_,_ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton("no"){dialogFace,_ -> dialogFace.cancel()}
            .create()
            .show()
    }

}