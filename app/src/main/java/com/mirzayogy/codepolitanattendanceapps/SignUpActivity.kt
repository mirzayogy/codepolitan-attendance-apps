package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editTextEmailSignUp

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initActionBar()

        btnSignUp.setOnClickListener {

            val email = editTextEmailSignUp.text.toString().trim()
            val password = editTextPasswordSignUp.text.toString().trim()
            val confirmPassword = editTextConfirmPasswordSignUp.text.toString().trim()

            CustomDialog.showLoading(this)
            if(checkValidation(email,password,confirmPassword)){
                registerToServer(email,password)
            }

        }

        tbSignUp.setNavigationOnClickListener {
            finish()
        }
    }

    private fun registerToServer(email: String, password: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                CustomDialog.hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkValidation(email: String, password: String, confirmPassword: String): Boolean {
        if(email.isEmpty()){
            editTextEmailSignUp.error = getString(R.string.please_fill_in_your_email)
            editTextEmailSignUp.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailSignUp.error = getString(R.string.please_use_valid_email)
            editTextEmailSignUp.requestFocus()
        }else if(password.isEmpty()){
            editTextPasswordSignUp.error = getString(R.string.please_fill_in_your_password)
            editTextPasswordSignUp.requestFocus()
        }else if(confirmPassword.isEmpty()){
            editTextConfirmPasswordSignUp.error  = getString(R.string.please_fill_in_your_confirmation_password)
            editTextConfirmPasswordSignUp.requestFocus()
        }else if(password != confirmPassword) {
            editTextConfirmPasswordSignUp.error = getString(R.string.your_password_does_not_match)
            editTextConfirmPasswordSignUp.requestFocus()
        }else if(editTextPasswordSignUp.text.toString().length < 6){
            editTextPasswordSignUp.error = getString(R.string.fill_in_password_at_least_6_characters)
            editTextPasswordSignUp.requestFocus()
        }else{
            return true
        }

        CustomDialog.hideLoading()
        return false
    }

    private fun initActionBar() {
        setSupportActionBar(tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}