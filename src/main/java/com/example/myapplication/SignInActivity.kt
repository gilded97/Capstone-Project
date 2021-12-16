package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var editEmail : EditText
    private lateinit var editPassword : EditText
    private lateinit var btnSignIn : Button
    private lateinit var btnSignUp : Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide() //Hides action bar at top of App

        //Initialize Firebase Authorization
        mAuth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.editText_email)
        editPassword = findViewById(R.id.editText_password)
        btnSignIn = findViewById(R.id.btn_sign_in_intro)
        btnSignUp = findViewById(R.id.btn_sign_up_intro)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            login(email, password)
        }

    }

    private fun login(email: String, password: String) {
        //Logic for logging in user

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for logging in user

                    val intent = Intent(this@SignInActivity,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignInActivity, "User does not exist", Toast.LENGTH_SHORT).show()

                }
            }

    }

}