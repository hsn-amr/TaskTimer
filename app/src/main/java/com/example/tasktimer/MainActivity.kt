package com.example.tasktimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.tasktimer.viewmodel.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val taskViewModel by lazy { TaskViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskViewModel.deactivateAllTasks()

        //adding the functionality to bottom navigation menu and attaching the fragments
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fcView)
        //bottomNavView.setupWithNavController(navController)
        ///////////////END of Bottom Navigation///////////////////////////

        bottomNavView.setOnNavigationItemSelectedListener { item ->  // using lamda
            when(item.itemId){
                R.id.viewFragment -> {
                    navController.navigate(R.id.viewFragment)
                }
                R.id.newFragment -> {
                    navController.navigate(R.id.newFragment)
                }
                R.id.totalTimeFragment -> {
                    navController.navigate(R.id.totalTimeFragment)
                }
            }
            true
        }
    }

    override fun onStop() {
        taskViewModel.deactivateAllTasks()
        super.onStop()
        taskViewModel.deactivateAllTasks()
        super.onDestroy()
    }

}