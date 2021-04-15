package com.example.iotuidesign

import android.graphics.Color
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
    private lateinit var sensorUltra: ValueEventListener

    var myRefs = database.getReference("Ultrasonic")

    private var progressBar: ProgressBar? = null

    val tag = "DebuggingIOT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watertank)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Water Tank"
        actionBar.setDisplayHomeAsUpEnabled(true)
        Handler(Looper.getMainLooper()).postDelayed({
            getUltraData()
        }, 2000)
    }

    private fun getUltraData() {
        progressBar = findViewById<ProgressBar>(R.id.indicator)

        sensorUltra = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(tag, "${p0.toException()}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                /*
                if (p0.exists()) {
                    val child = p0.children
                    for (data in child) {
                        var cond = data.child("Condition").value
                        var vol = data.child("Volume").value

                        sensorCond = data.value.toString()
                        sensorData = data.value.toString()

                        water_value.text    = vol.toString()
                        txtDetail.text      = cond.toString()

                        /*
                        sensorData = data.child("Volume").value.toString()
                        water_value.text = sensorData.toInt().toString()
                        progressBar!!.progress = sensorData.toDouble().toInt() / 5
                        percentage.text = (sensorData.toDouble() / 5).toString()
                        */
                    }
                }

                 */

                //1 node
                water_value.text=" "
                val vol = p0.child("Volume").value.toString()
                val cond = p0.child("Condition").value
                val det = p0.child("Details").value

                water_value.text    = vol.toInt().toString()
                txtCond.text        =cond.toString()
                txtDetail.text      = det.toString()

                if (txtCond.text != "Good"){
                    rgbdot.setTextColor(Color.parseColor("#B3000C"))
                }else {
                    rgbdot.setTextColor(Color.parseColor("#00B32C"))
                }
                progressBar!!.progress = vol.toString().toInt() / 5
                percentage.text = (vol.toInt() / 5).toString()
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

    /*
    @SuppressLint("SetTextI18n")
    private fun setStatus(status: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                status < 100.toString() -> {
                    txtDetail.text =
                        "There's a problem with the water tank. Water level is below 100 Litre"
                }
                status <= 475.toString() -> {
                    txtDetail.text =
                        "There's a problem with the water tank. Water level is over 475 Litre"
                }
                status >= 100.toString() && status <= 475.toString() -> {
                    txtDetail.text = "Water tank status is good."
                }
                else -> {
                    txtDetail.text = "Error! Please manually check the water tank."
                }
            }
        }, 500)
    }
    */
}

