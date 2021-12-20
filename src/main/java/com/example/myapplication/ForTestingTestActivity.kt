package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

private lateinit var emailEditText: EditText
private lateinit var resetPasswordButton: Button
private lateinit var progressBar: ProgressBar
private lateinit var auth: FirebaseAuth

class ForTestingTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_testing_test)

        emailEditText = findViewById(R.id.email)
        resetPasswordButton = findViewById(R.id.resetPassword)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()

        resetPasswordButton.setOnClickListener{
            resetPassword()
        }
    }

    private fun resetPassword() {
        var email = emailEditText.text.toString().trim()
        if(email.isEmpty()){
            emailEditText.setError("Email is required!")
            emailEditText.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!")
            emailEditText.requestFocus()
            return
        }
        progressBar.setVisibility(View.VISIBLE)
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email to change your password!", Toast.LENGTH_LONG).show()
                progressBar.setVisibility(View.GONE)
            }else{
                Toast.makeText(this, "Some error occured, please try again.", Toast.LENGTH_LONG).show()
                progressBar.setVisibility(View.GONE)
            }
        }
    }
}