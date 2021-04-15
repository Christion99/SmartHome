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
import kotlinx.android.synthetic.main.activity_motion.*

class Motion : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private lateinit var sensorMotion: ValueEventListener

    var myRefs = database.getReference("Motion Sensor")
    val tag = "DebuggingIOT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Motion"
        actionBar.setDisplayHomeAsUpEnabled(true)
        Handler(Looper.getMainLooper()).postDelayed({
            getMotionData()
        }, 2000)
    }

    private fun getMotionData(){
        sensorMotion = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(tag, "${p0.toException()}")
            }

            override fun onDataChange(p0: DataSnapshot) {

                val bStatus = p0.child("Bathroom Status").value.toString()
                val datetime = p0.child("DateAndTime").value.toString()
                val dStatus = p0.child("Door Status").value.toString()
                val fStatus = p0.child("Fan Status").value.toString()
                val lStatus = p0.child("Light Status").value.toString()
                val mStatus = p0.child("Motion").value.toString()

                detailState.text = bStatus
                dtState.text = datetime
                motionState.text = mStatus
                lightState.text = lStatus
                fanState.text = fStatus
                doorState.text = dStatus

                //door
                when (doorState.text) {
                    "Lock" -> {
                        swDoor.isChecked=true
                    }
                    else -> {
                        swDoor.isChecked=false
                    }
                }

                //fan
                when {
                    fanState.text != "OFF" -> {
                        swFan.isChecked=true
                    }
                    else -> {
                        swFan.isChecked=false
                    }
                }

                //light
                when {
                    lightState.text != "OFF" -> {
                        swLight.isChecked=true
                    }
                    else -> {
                        swLight.isChecked=false
                    }
                }

            }
        }
        myRefs.addValueEventListener(sensorMotion)
    }

    private fun destroyListeners() {
        myRefs.removeEventListener(sensorMotion)
    }

    override fun onDestroy() {
        Log.d(tag, "MotionOnDestroy")
        destroyListeners()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}