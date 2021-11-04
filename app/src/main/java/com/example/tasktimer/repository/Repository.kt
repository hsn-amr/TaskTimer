package com.example.tasktimer.repository

import androidx.lifecycle.LiveData
import com.example.tasktimer.database.TaskDao
import com.example.tasktimer.model.Task

class Repository(private val taskDao: TaskDao) {

    val readAllTasks: LiveData<List<Task>> = taskDao.readAllTasks()

    suspend fun addTask(task: Task){
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun deactivateAllTasks(){
        taskDao.deactivateAllTasks()
    }
}