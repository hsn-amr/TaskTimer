package com.example.tasktimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val task: String,
    val description: String,
    val timer: Long,
    val totalTime: Long,
    val active: Boolean
)
