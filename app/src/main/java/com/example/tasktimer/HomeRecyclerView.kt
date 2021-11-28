package com.example.tasktimer

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.item_row.view.*
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import androidx.core.view.isVisible
import com.example.tasktimer.fragments.ViewFragment
import kotlinx.android.synthetic.main.fragment_view.*

class HomeRecyclerView(application: Application, val viewFragment: ViewFragment):
    RecyclerView.Adapter<HomeRecyclerView.ItemViewHolder>() {
    class ItemViewHolder( itemView: View): RecyclerView.ViewHolder(itemView)

    private var tasks = emptyList<Task>()
    private val taskViewModel by lazy { TaskViewModel(application) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val mainTimer = viewFragment.tvTimemain

        holder.setIsRecyclable(false)
        val task = tasks[position]

        holder.itemView.apply {

            tvTitleInHome.text = task.task
            chronometer.text = task.timer

            if(task.active && task.isClicked){
                viewFragment.mainTitle.text = task.task
                viewFragment.mainDescription.text = task.description
                mainTimer.base = SystemClock.elapsedRealtime() - task.pauseOffset
                mainTimer.start()

                llOptionsHolder.isVisible = true
                task.isClicked = false
                taskViewModel.updateTask(task)
            }else if (task.active && !task.isClicked){
                mainTimer.onChronometerTickListener = Chronometer.OnChronometerTickListener { mainChronometer ->
                    chronometer.text = mainChronometer.text
                }
            }else if (!task.active && !task.isClicked){
                llOptionsHolder.isVisible = false
            }


            tvTitleInHome.setOnClickListener {
                if(!task.active){
                    for (activeTask in tasks){
                        if(activeTask.active){
                            stopTimer(activeTask, mainTimer)
                        }
                    }
                    viewFragment.mainTitle.text = task.task
                    taskViewModel.deactivateAllTasks()
                    task.active = true
                    task.isClicked = true
                    taskViewModel.updateTask(task)
                }
            }

            btnStop.setOnClickListener {
                stopTimer(task, mainTimer)
            }
            btnRestart.setOnClickListener {
                restartTimer(task, mainTimer)
            }

        }

    }

    private fun stopTimer(task: Task, chronometer: Chronometer){
        task.timer = chronometer.text.toString()
        chronometer.stop()
        task.pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
        task.active = false
        taskViewModel.updateTask(task)
    }

    private fun restartTimer(task:Task, chronometer: Chronometer){
        task.totalTime = getTotalFromString(task.totalTime, chronometer.text.toString())
        task.timer = "00:00"
        chronometer.base = SystemClock.elapsedRealtime()
        task.pauseOffset = 0
        chronometer.stop()
        task.active = false
        taskViewModel.updateTask(task)
        viewFragment.mainTitle.text = ""
        viewFragment.mainDescription.text = ""
    }

    override fun getItemCount() = tasks.size

    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

    private fun getTotalFromString(oldString: String, newString: String): String {
        var oldHours = 0
        var oldMinutes = 0
        var oldSeconds = 0
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
        var newMinutes = 0
        var newSeconds = 0
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

}