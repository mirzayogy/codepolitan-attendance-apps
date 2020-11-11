package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initFirebaseAuth()
        getData()

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,AuthActivity::class.java))
            finish()
        }
    }

    private fun getData() {
        val user = auth.currentUser

        if(user!=null){
            textViewEmail.text = user.email
        }
    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }
}