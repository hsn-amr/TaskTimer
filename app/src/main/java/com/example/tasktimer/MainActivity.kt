package com.example.tasktimer

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    lateinit var tLayout:TabLayout//you may need when changing the fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //adding the functionality to bottom navigation menu and attaching the fragments
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fcView)
        //bottomNavView.setupWithNavController(navController)
        ///////////////END of Buttom Navigation///////////////////////////

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

}