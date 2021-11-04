package com.example.tasktimer

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.activity_details.view.tvTime
import kotlinx.android.synthetic.main.activity_details.view.tvTitle
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*
import java.util.concurrent.TimeUnit

import android.R.string.no
import android.app.Activity
import android.util.Log


class HomeRecyclerView(application: Application, val activity: Activity): RecyclerView.Adapter<HomeRecyclerView.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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

        val task = tasks[position]

        holder.itemView.apply {
            tvTitleInHome.text = task.task
            tvTime.text = task.timer


        }
    }

    override fun getItemCount() = tasks.size

    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }
}