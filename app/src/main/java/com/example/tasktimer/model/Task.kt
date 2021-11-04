package com.example.tasktimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var task: String,
    var description: String,
    val timer: String,
    val totalTime: String,
    var active: Boolean
)
