package com.example.tasktimer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tasktimer.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  addTask(task: Task)

    @Query("SELECT * FROM taskTable ORDER BY id ASC")
    fun readAllTasks(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}