package com.example.tasktimer.interfaces

import com.example.tasktimer.model.Task

interface ActiveTask {

    fun setTitle(title: String)

    fun setDescription(description: String)

    fun startTimer(task: Task)

    fun stopTimer(task: Task)

    fun restartTimer(task: Task)

    fun onTimerClick(task: Task)

    fun onTimerTickListener(callBack: (time: String) -> Unit)

}