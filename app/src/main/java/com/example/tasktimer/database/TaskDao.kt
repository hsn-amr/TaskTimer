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

    @Query("UPDATE taskTable SET active = :inactive WHERE active = :active")
    fun deactivateAllTasks(inactive: Boolean = false, active: Boolean = true)

    @Query("SELECT * FROM taskTable WHERE active = :active LIMIT 1")
    fun getTaskByActive(active: Boolean = true): Task

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}