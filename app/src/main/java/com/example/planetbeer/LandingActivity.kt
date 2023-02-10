package com.example.planetbeer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LandingActivity : AppCompatActivity() {
    private lateinit var getStarted: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        getStarted=findViewById(R.id.getStarted)
        getStarted.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}