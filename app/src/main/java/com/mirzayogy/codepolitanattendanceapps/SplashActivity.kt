package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuth()
            startActivity(Intent(this,AuthActivity::class.java))
        },1200)
    }

    private fun checkAuth() {
        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this,DashboardActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this,AuthActivity::class.java))
            finish()
        }
    }
}