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
import android.widget.Chronometer
import androidx.core.view.doOnDetach
import androidx.core.view.isVisible
import com.example.tasktimer.fragments.ViewFragment
import kotlinx.android.synthetic.main.fragment_view.view.*

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

        holder.setIsRecyclable(false)
        val task = tasks[position]

        holder.itemView.apply {
            tvTitleInHome.text = task.task

            chronometer.text = task.timer
            if(position == tasks.size-1){
                viewFragment.isFirstTime = false
            }

            if(task.active){
                chronometer.text = task.timer
                viewFragment.mainTitle.text = task.task
                chronometer.base = SystemClock.elapsedRealtime() - task.pauseOffset
                chronometer.start()
                chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener { chronometer ->
                    viewFragment.mainTime.text = chronometer.text
                }
            }

            tvTitleInHome.setOnClickListener {

                viewFragment.mainTitle.text = task.task

                if(!task.active){
                    viewFragment.stopAllOtherTimers(viewFragment.tasks, task.id)

                    chronometer.base = SystemClock.elapsedRealtime() - task.pauseOffset
                    chronometer.start()
                    task.active = true
                    taskViewModel.updateTask(task)
                    viewFragment.showingButtons(false)
                }else{
                    task.timer = chronometer.text.toString()
                    chronometer.stop()
                    task.pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                    task.active = false
                    Log.e("TAG","${task.timer}")
                    chronometer.text = task.timer
                    taskViewModel.updateTask(task)
                    viewFragment.showingButtons(true)
                }

                chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener { chronometer ->
                    viewFragment.mainTime.text = chronometer.text
                }

                viewFragment.pauseButton.setOnClickListener {
                    chronometer.base = SystemClock.elapsedRealtime() - task.pauseOffset
                    chronometer.start()
                    task.active = true
                    taskViewModel.updateTask(task)
                    viewFragment.showingButtons(false)
                }

                viewFragment.restartButton.setOnClickListener {
                    task.totalTime = getTotalFromString(task.totalTime, chronometer.text.toString())
                    chronometer.base = SystemClock.elapsedRealtime()
                    task.pauseOffset = 0
                    chronometer.start()
                    task.active = true
                    taskViewModel.updateTask(task)
                    viewFragment.showingButtons(false)
                }
            }
        }

    }


    override fun getItemCount() = tasks.size

    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun getTotalFromString(oldString:String, newString: String): String {
        var oldHours = 0
        var oldMinutes = 0
        var oldSeconds = 0
        val oldStringArray = oldString.split(":")
        if(oldStringArray.size == 2){
            oldMinutes = oldStringArray[0].toInt()
            oldSeconds = oldStringArray[1].toInt()
        }else{
            oldHours = oldStringArray[0].toInt()
            oldMinutes = oldStringArray[1].toInt()
            oldSeconds = oldStringArray[2].toInt()
        }

        var newHours = 0
        var newMinutes = 0
        var newSeconds = 0
        val newStringArray = newString.split(":")
        if(newStringArray.size == 2){
            newMinutes = newStringArray[0].toInt() + oldMinutes
            newSeconds = newStringArray[1].toInt() + oldSeconds
        }else{
            newHours = newStringArray[0].toInt() + oldHours
            newMinutes = newStringArray[1].toInt() + oldMinutes
            newSeconds = newStringArray[2].toInt() + oldSeconds
        }

        if(newSeconds>=60){
            newMinutes += newSeconds/60
            newSeconds %= 60
        }
        if(newMinutes>=60){
            newHours += newMinutes/60
            newMinutes %= 60
        }

        val result = (if (newHours < 10) "0$newHours" else newHours).toString() +
                ":" + (if (newMinutes < 10) "0$newMinutes" else newMinutes) +
                ":" + if (newSeconds < 10) "0$newSeconds" else newSeconds

        return result
    }

}