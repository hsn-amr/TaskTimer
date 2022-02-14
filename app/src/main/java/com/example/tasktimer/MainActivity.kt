package com.example.tasktimer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var view1 : View
    private lateinit var fcView: View
    private lateinit var sharedPreferences: SharedPreferences
    private var instructions = 0

    private lateinit var imageButton: ImageView
    private lateinit var tvInstructions: TextView

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        bottomNavView.setOnItemSelectedListener { item ->  // using lambda
            when(item.itemId){
                R.id.viewFragment -> {
                    navController.navigate(R.id.viewFragment)
                }
                R.id.newFragment -> {
                    userMoved()
                    navController.navigate(R.id.newFragment)
                }
                R.id.totalTimeFragment -> {
                    userMoved()
                    navController.navigate(R.id.totalTimeFragment)
                }
            }
            true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Timer Paused", Toast.LENGTH_LONG).show()
    }
    private fun userMoved(){
        with(sharedPreferences.edit()){
            putBoolean("userMoved", true)
            apply()
        }
    }

    //** user instructions
    private fun instructions(){
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