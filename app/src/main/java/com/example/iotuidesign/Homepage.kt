package com.example.iotuidesign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_homepage.*

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        btnTank.setOnClickListener{
            val intent = Intent (this, Watertank::class.java)
            startActivity(intent)
        }

        btnMotion.setOnClickListener{
            val intent = Intent (this, Motion::class.java)
            startActivity(intent)
        }

        btnTemp.setOnClickListener{
            val intent = Intent (this, DHT::class.java)
            startActivity(intent)
        }
    }
}