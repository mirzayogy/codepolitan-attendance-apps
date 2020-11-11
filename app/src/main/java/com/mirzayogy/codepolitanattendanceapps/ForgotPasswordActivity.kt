package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        initActionBar()

        btnSendEmail.setOnClickListener {
            val email = editTextEmailForgot.text.toString().trim()
            if(email.isEmpty()){
                editTextEmailForgot.error = getString(R.string.please_fill_in_your_email)
                editTextEmailForgot.requestFocus()
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmailForgot.error = getString(R.string.please_use_valid_email)
                editTextEmailForgot.requestFocus()
            }else{
                forgotPassword(email)
            }
            Toast.makeText(this,getString(R.string.send_email),Toast.LENGTH_SHORT).show()
        }

        tbForgotPassword.setNavigationOnClickListener {
            finish()
        }
    }

    private fun forgotPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Sent to email",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,SignInActivity::class.java))
                    finishAffinity()
                }else{
                    Toast.makeText(this, "Reset failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }
    }

    private fun initActionBar() {
        setSupportActionBar(tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}