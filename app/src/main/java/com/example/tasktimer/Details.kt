package com.example.tasktimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Details : AppCompatActivity() {
    lateinit var tvTitle: TextView //initialization
    lateinit var tvDesc:TextView
    lateinit var btSave: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        tvTitle=findViewById(R.id.tvTitle)
        tvDesc=findViewById(R.id.tvDesc)
        btSave=findViewById(R.id.btSave)
    }

}