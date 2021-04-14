package com.example.iotuidesign

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_watertank.*

class Watertank : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private lateinit var sensorData: String
    private lateinit var sensorUltra: ValueEventListener
    var myRefs = database.getReference("PI_007").child("Ultrasonic")
    //private val path = "UltraSonic"

    private var progressBar: ProgressBar? = null

    val tag = "DebuggingIOT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watertank)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Water Tank"
        actionBar.setDisplayHomeAsUpEnabled(true)
        Handler(Looper.getMainLooper()).postDelayed({
            getUltraDate()

        }, 2000)
    }

    private fun getUltraDate() {
        progressBar = findViewById<ProgressBar>(R.id.indicator)

        sensorUltra = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(tag, "${p0.toException()}")
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                val data = p0.value
                water_value.text = data.toString()

                val mainHandler = Handler(Looper.getMainLooper(), Handler.Callback {
                    setStatus(water_value.toString())
                    true
                })

                progressBar!!.progress = data.toString().toInt() / 5
                percentage.text = (data.toString().toInt() / 5).toString()
                val status = percentage.toString()
                setStatus(status)
                //val status = percentage.toString()
                //setStatus(status)
                //setStatus(water_value.toString())

            }
        }
        myRefs.addValueEventListener(sensorUltra)
    }

    private fun destroyListeners() {
        myRefs.removeEventListener(sensorUltra)
    }

    override fun onDestroy() {
        Log.d(tag, "UltrasonicOnDestroy")
        destroyListeners()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setStatus(status: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (status < 100.toString()) {
                txtDetail.text =
                    "There's a problem with the water tank. Water level is below 100 Litre"
            } else if (status <= 475.toString()) {
                txtDetail.text =
                    "There's a problem with the water tank. Water level is over 475 Litre"
            } else if (status >= 100.toString() && status <= 475.toString()) {
                txtDetail.text = "Water tank status is good."
            } else {
                txtDetail.text = "Error! Please manually check the water tank."
            }
        }, 500)
    }
}

