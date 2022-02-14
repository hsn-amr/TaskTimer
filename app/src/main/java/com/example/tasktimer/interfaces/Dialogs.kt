package com.example.tasktimer.interfaces

import com.example.tasktimer.model.Task

interface Dialogs {

    fun showUpdateTaskAlterDialog(task: Task)

    fun showDeleteTaskAlertDialog(task: Task)
}