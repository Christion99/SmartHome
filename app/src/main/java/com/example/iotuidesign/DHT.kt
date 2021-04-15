package com.example.iotuidesign

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dht.*

class DHT : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private lateinit var sensorDHT: ValueEventListener

    var myRefs = database.getReference("DHT Sensor")
    val tag = "DebuggingIOT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dht)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "DHT"
        actionBar.setDisplayHomeAsUpEnabled(true)
        Handler(Looper.getMainLooper()).postDelayed({
            getDHTData()
        }, 2000)
    }

    private fun getDHTData() {
        sensorDHT = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(tag, "${p0.toException()}")
            }

            override fun onDataChange(p0: DataSnapshot) {

                val fStatus = p0.child("Fan Status").value.toString()
                val datetime = p0.child("Current date and time").value.toString()
                val tStatus = p0.child("Temperature").value.toString()
                val rTimeStatus = p0.child("Remaining Time").value.toString()
                val hStatus = p0.child("Humidity").value.toString()

                iddate.text = datetime
                idfan.text = fStatus
                idhum.text = hStatus
                idtimerem.text = rTimeStatus
                idtemp.text = tStatus
            }
        }
        myRefs.addValueEventListener(sensorDHT)
    }

    private fun destroyListeners() {
        myRefs.removeEventListener(sensorDHT)
    }

    override fun onDestroy() {
        Log.d(tag, "DHTOnDestroy")
        destroyListeners()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}