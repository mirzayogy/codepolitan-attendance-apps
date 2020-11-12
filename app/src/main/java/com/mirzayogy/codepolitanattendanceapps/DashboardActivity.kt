package com.mirzayogy.codepolitanattendanceapps

import android.content.ContentProviderClient
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initFirebaseAuth()
        getData()

        btnLogout.setOnClickListener {
            auth.signOut()

            mGoogleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(this, getString(R.string.succes_fully_signed_out),Toast.LENGTH_SHORT).show()
            }

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
    }
}