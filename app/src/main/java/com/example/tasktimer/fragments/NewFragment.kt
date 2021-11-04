package com.example.tasktimer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tasktimer.R
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)

        val taskViewModel by lazy { TaskViewModel(requireActivity().application) }

        val taskTitleInput = view.findViewById<EditText>(R.id.etTitle)
        val taskDescriptionInput = view.findViewById<EditText>(R.id.etDesc)
        val saveButton = view.findViewById<Button>(R.id.btSave)

        saveButton.setOnClickListener {
            val taskTitle = taskTitleInput.text.toString()
            val taskDescription = taskDescriptionInput.text.toString()

            if(taskTitle.isNotEmpty() && taskDescription.isNotEmpty()){
                val task = Task(0,taskTitle,taskDescription,"00:00:00","00:00:00",false)
                taskViewModel.addTask(task)

                taskTitleInput.text.clear()
                taskDescriptionInput.text.clear()

                Toast.makeText(requireContext(),"The Task Has Saved", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(requireContext(),"Please, Enter Full Information", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

}