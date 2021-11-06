package com.example.tasktimer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.tasktimer.fragments.ViewFragment
import com.example.tasktimer.viewmodel.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var view1 : View
    lateinit var fcView: View
    private lateinit var sharedPreferences: SharedPreferences
    var instructions = 0

    lateinit var imageButton: ImageView
    lateinit var tvInstructions: TextView

    private val taskViewModel by lazy { TaskViewModel(application) }
    lateinit var bottomNavView: BottomNavigationView
    val fragment = ViewFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskViewModel.deactivateAllTasks()

        //adding the functionality to bottom navigation menu and attaching the fragments
        bottomNavView = findViewById(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fcView)

        //***user instructions
        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener{
            tvInstructions = findViewById(R.id.tvInstructions)
            tvInstructions.isVisible = !tvInstructions.isVisible
            tvInstructions.setOnClickListener { tvInstructions.isVisible = false
                instructions() }
        }

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), MODE_PRIVATE
        )
        instructions = sharedPreferences.getInt("instructions", 0)

        if (instructions == 0){
            instructions()
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

    //** user instructions
    fun instructions(){
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


}