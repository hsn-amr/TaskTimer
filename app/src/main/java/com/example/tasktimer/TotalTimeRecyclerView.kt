package com.example.tasktimer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.interfaces.Dialogs
import com.example.tasktimer.model.Task
import kotlinx.android.synthetic.main.totaltime_row.view.*

class TotalTimeRecyclerView(private val dialog: Dialogs):
    RecyclerView.Adapter<TotalTimeRecyclerView.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var tasks = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.totaltime_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val task = tasks[position]

        holder.itemView.apply {
            tvTotalTitle.text = task.task
            tvTotalTime.text = task.totalTime
            tvTotatlDescription.text = task.description

            llDescriptionHolder.isVisible = false

            ivTotalMore.setOnClickListener {
                llDescriptionHolder.isVisible = !llDescriptionHolder.isVisible
            }

            btnUpdate.setOnClickListener { dialog.showUpdateTaskAlterDialog(task) }
            btnDelete.setOnClickListener { dialog.showDeleteTaskAlertDialog(task) }
        }
    }

    override fun getItemCount() = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

}