package com.example.tasktimer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.model.Task
import kotlinx.android.synthetic.main.item_row.view.*
import androidx.core.view.isVisible
import com.example.tasktimer.interfaces.ActiveTask

class HomeRecyclerView(private val activeTask: ActiveTask):
    RecyclerView.Adapter<HomeRecyclerView.ItemViewHolder>() {
    class ItemViewHolder( itemView: View): RecyclerView.ViewHolder(itemView)

    private var tasks = emptyList<Task>()

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

            if(task.active && task.isClicked){
                activeTask.startTimer(task)
                llOptionsHolder.isVisible = true

            }else if (task.active && !task.isClicked){
                activeTask.onTimerTickListener {
                    time -> chronometer.text = time
                }
//                mainTimer.onChronometerTickListener = Chronometer.OnChronometerTickListener { mainChronometer ->
//                    chronometer.text = mainChronometer.text
//                    Log.e("TAG",checkForBreakTime(mainChronometer.text.toString()).toString())
//                    if (checkForBreakTime(mainChronometer.text.toString())){
//                        viewFragment.pushNotification()
//                    }
//                }
            }else if (!task.active && !task.isClicked){
                llOptionsHolder.isVisible = false
            }


            tvTitleInHome.setOnClickListener {
                if(!task.active){
                    for (activeTaskInTasks in tasks){
                        if(activeTaskInTasks.active){
                            activeTask.stopTimer(activeTaskInTasks)
                        }
                    }
                    activeTask.onTimerClick(task)
                }
            }

            btnStop.setOnClickListener {
                activeTask.stopTimer(task)
            }
            btnRestart.setOnClickListener {
                activeTask.restartTimer(task)
            }

        }

    }

    override fun getItemCount() = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

}