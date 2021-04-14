package com.example.iotuidesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_watertank.*

class Watertank : AppCompatActivity(){

    private var database = FirebaseDatabase.getInstance()
    private lateinit var sensorData:String
    private lateinit var sensorUltra: ValueEventListener
    private val path = "PI_007/UltraSonic"
    private var myRefs = database.getReference(path).child(path).orderByKey().limitToLast(1)

    private var progressBar: ProgressBar? = null

    val tag = "DebuggingIOT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watertank)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Water Tank Reserve"
        actionBar.setDisplayHomeAsUpEnabled(true)

        getUltraDate()
    }

    private fun getUltraDate() {
        progressBar = findViewById<ProgressBar>(R.id.indicator)
        sensorUltra = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(tag, "${p0.toException()}")
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val child = p0.children
                    for (data in child){
                        sensorData = data.child("ultra2").value.toString()//change the path variable
                        water_value.text= (sensorData.toDouble()).toString()

                        progressBar!!.progress = sensorData.toDouble().toInt()/5
                        percentage.text= (sensorData.toDouble()/5).toString()

                    }
                }
            }
        }
        myRefs.addValueEventListener(sensorUltra)
    }

    private fun destroyListeners(){
        myRefs.removeEventListener(sensorUltra)
    }

    override fun onDestroy() {
        Log.d(tag, "ThermometerOnDestroy")
        destroyListeners()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}