package com.example.tasktimer

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer.model.Task
import com.example.tasktimer.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.totaltime_row.view.*

class TotalTimeRecyclerView(application: Application, private val context: Context):
    RecyclerView.Adapter<TotalTimeRecyclerView.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var tasks = emptyList<Task>()
    private val taskViewModel by lazy { TaskViewModel(application) }

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

            btnUpdate.setOnClickListener { updateDialog(task) }
            btnDelete.setOnClickListener { deleteDialog(task) }
        }
    }

    override fun getItemCount() = tasks.size

    fun update(tasks: List<Task>){
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun updateDialog(task: Task){
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val taskTitle = EditText(context)
        val taskDescription = EditText(context)
        taskTitle.setText(task.task)
        taskDescription.setText(task.description)

        layout.addView(taskTitle)
        layout.addView(taskDescription)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Update Task")
            .setView(layout)
            .setPositiveButton("Update"){_,_ ->
                task.task = taskTitle.text.toString()
                task.description = taskDescription.text.toString()

                taskViewModel.updateTask(task)
            }
            .setNegativeButton("Cancel"){dialogFace,_ -> dialogFace.cancel()}
            .create()
        dialog.show()
    }

    fun deleteDialog(task: Task){
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete Task")
            .setMessage("Do You Want To Delete This Task")
            .setPositiveButton("yes"){_,_ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton("no"){dialogFace,_ -> dialogFace.cancel()}
            .create()
        dialog.show()
    }
}