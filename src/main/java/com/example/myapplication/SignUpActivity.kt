package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var editName : EditText
    private lateinit var editEmail : EditText
    private lateinit var editPassword : EditText
    private lateinit var btnSignUp : Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDBReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide() //Hides action bar at top of App

        mAuth = FirebaseAuth.getInstance()

        editName = findViewById(R.id.editText_name)
        editEmail = findViewById(R.id.editText_email)
        editPassword = findViewById(R.id.editText_password)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            //Get details of User
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            signUp(name, email, password)
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        //Logic for Signing up User
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home

                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpActivity, "Some error occurred", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        //Initialize DB Reference
        mDBReference = FirebaseDatabase.getInstance().getReference()

        //"user" is parent node in path
        mDBReference.child("user").child(uid).setValue(User(name, email, uid))

    }


}