package com.example.tasktimer.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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
import android.os.Handler
import android.widget.Chronometer
import androidx.core.view.isVisible
import com.example.tasktimer.viewmodel.TaskViewModel
import java.util.*
import android.util.Log

import android.os.CountDownTimer
import com.example.tasktimer.TimerService


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mainTitle: TextView
    lateinit var mainTime: TextView
    lateinit var pauseButton: Button
    lateinit var restartButton: Button

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

        val taskViewModel by lazy { TaskViewModel(requireActivity().application) }

        taskViewModel.deactivateAllTasks()
        mainTitle = view.findViewById(R.id.tvTotalmain)
        mainTime = view.findViewById(R.id.tvTimemain)
        pauseButton = view.findViewById(R.id.btnPause)
        restartButton = view.findViewById(R.id.btnRestart)
        showingButtons(false)

        val rvMain = view.findViewById<RecyclerView>(R.id.rvMain)
        val adapter = HomeRecyclerView(requireActivity().application, requireActivity())
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.getAllTasks().observe(viewLifecycleOwner, {
                allTasks -> adapter.update(allTasks)
        })

        return view
    }

    fun showingButtons(state: Boolean){
        pauseButton.isVisible = state
        restartButton.isVisible = state
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewFragment.
         */
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