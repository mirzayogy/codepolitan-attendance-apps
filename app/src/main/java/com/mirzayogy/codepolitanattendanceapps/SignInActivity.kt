package com.mirzayogy.codepolitanattendanceapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object{
        const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initActionBar()
        initFirebaseAuth()

        btnSignIn.setOnClickListener {

            val email = editTextEmailSignIn.text.toString()
            val password = editTextPasswordSignIn.text.toString()

            if(checkValidation(email,password)){
                loginToServer(email,password)
            }
        }

        btnForgotPassword.setOnClickListener {
            val i = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(i)
        }

        btnGoogleSignIn.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        tbSignIn.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            CustomDialog.showLoading(this)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
                firebaseAuth(credential)

            }catch (e: ApiException){
                CustomDialog.hideLoading()
                Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun loginToServer(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email,password)
        firebaseAuth(credential)
    }

    private fun firebaseAuth(credential: AuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener {
                    CustomDialog.hideLoading()
                    if(it.isSuccessful){
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finishAffinity()
                    }else{
                        Toast.makeText(this,getString(R.string.sign_in_failed),Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        if(email.isEmpty()){
            editTextEmailSignIn.error = getString(R.string.please_fill_in_your_email)
            editTextEmailSignIn.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailSignIn.error = getString(R.string.please_use_valid_email)
            editTextEmailSignIn.requestFocus()
        }else if(password.isEmpty()){
            editTextPasswordSignIn.error = getString(R.string.please_fill_in_your_password)
            editTextPasswordSignIn.requestFocus()
        }else if(password.length < 6){
            editTextPasswordSignIn.error = getString(R.string.fill_in_password_at_least_6_characters)
            editTextPasswordSignIn.requestFocus()
        }else{
            return true
        }
        CustomDialog.hideLoading()
        return false
    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
    }

    private fun initActionBar() {
        setSupportActionBar(tbSignIn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}

//firebase -> authentication -> sign-in method -> google
// ./gradlew signingReport
//9B:D4:76:BE:59:AE:1F:B9:21:25:3E:50:96:BE:1F:94:87:3F:39:D4
//firebase -> project setting -> signing SHA certificate