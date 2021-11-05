package com.example.tasktimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tasktimer.database.TaskDatabase
import com.example.tasktimer.model.Task
import com.example.tasktimer.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAccessor

class TaskViewModel (application: Application): AndroidViewModel(application){

    private val readAllTasks: LiveData<List<Task>>
    private val repository: Repository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = Repository(taskDao)
        readAllTasks = repository.readAllTasks
    }

    fun getAllTasks(): LiveData<List<Task>>{
        return readAllTasks
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTask(task)
        }
    }

    fun deactivateAllTasks(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deactivateAllTasks()
        }
    }

}