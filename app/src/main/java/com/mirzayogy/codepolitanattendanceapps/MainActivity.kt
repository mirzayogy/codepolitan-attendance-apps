package com.mirzayogy.codepolitanattendanceapps

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.skyfishjy.library.RippleBackground
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClick()
    }


    private fun onClick(){
        fabCheckIn.setOnClickListener{
            loadScanLocation()
            Handler(Looper.getMainLooper()).postDelayed({
                stopScanLocation()
            },4000)
        }
    }

    private fun loadScanLocation(){
        rippleBackground.startRippleAnimation()
        textViewScanning.visibility = View.VISIBLE
        textViewCheckInSuccess.visibility = View.GONE
    }

    private fun stopScanLocation(){
        rippleBackground.stopRippleAnimation()
        textViewScanning.visibility = View.GONE
    }
}