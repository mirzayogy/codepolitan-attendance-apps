package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnSignInAuth.setOnClickListener{
            val i = Intent(this,SignInActivity::class.java)
            startActivity(i)
        }

        btnSignUpAuth.setOnClickListener{
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }
    }
}