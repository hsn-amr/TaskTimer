package com.example.tasktimer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.tasktimer.viewmodel.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var view1 : View
    lateinit var fcView: View
    private lateinit var sharedPreferences: SharedPreferences
    var instructions = 0

    private val taskViewModel by lazy { TaskViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskViewModel.deactivateAllTasks()

        //adding the functionality to bottom navigation menu and attaching the fragments
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fcView)

        //***user instructions
        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        instructions = sharedPreferences.getInt("instructions", 0)

        if (instructions == 0){
          imageView = findViewById(R.id.imageView1)
          imageView.isVisible = true

          fcView = findViewById(R.id.fcView)
          fcView.isVisible = false
          bottomNavView.isVisible = false

          view1 = findViewById(R.id.view1)
          view1.isVisible = true

          view1.setOnClickListener {
            imageView.setImageResource(R.drawable.three)
            view1.setOnClickListener {
                imageView.setImageResource(R.drawable.four)
                view1.setOnClickListener {
                    imageView.isVisible = false
                    fcView.isVisible = true
                    bottomNavView.isVisible = true
                    instructions = 1
                    with(sharedPreferences.edit()) {
                        putInt("instructions", instructions)
                        apply()
                    }
                }
            }
          }
        }
        //***

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