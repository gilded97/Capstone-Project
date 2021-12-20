package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var editName : EditText
    private lateinit var editEmail : EditText
    private lateinit var editPassword : EditText
    private lateinit var editPasswordConfirm : EditText
    private lateinit var editRollNumber : EditText
    private lateinit var editYear : EditText
    private lateinit var editDept : EditText
    private lateinit var editStudentId : EditText
    private lateinit var btnSignUp : Button
    private var isStudent : Boolean = false
    private var isFaculty : Boolean = false

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
        editPasswordConfirm = findViewById(R.id.editText_repeat_password)
        editRollNumber = findViewById(R.id.editText_rollnumber)
        editYear = findViewById(R.id.editText_year)
        editDept = findViewById(R.id.editText_dept)
        editStudentId = findViewById(R.id.editText_student_id_no)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            //Get details of User
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val passwordConfirm = editPasswordConfirm.text.toString()
            val rollNumber = editRollNumber.text.toString()
            val year = editYear.text.toString()
            val department = editDept.text.toString()
            val studentIdNumber = editStudentId.text.toString()

            var stringArray = arrayListOf(name, email, password, rollNumber, year, department, studentIdNumber)

            for(string in stringArray){
                if(string == null  || string.isEmpty()){
                    when(string){
                        name -> Toast.makeText(this, "Fill In First The Name Field",Toast.LENGTH_LONG).show()
                        email  ->   Toast.makeText(this, "Fill In The Email Field",Toast.LENGTH_LONG).show()
                        password -> Toast.makeText(this, "Fill In The Password Field",Toast.LENGTH_LONG).show()
                        passwordConfirm -> Toast.makeText(this, "Fill In The Password Confirmation Field",Toast.LENGTH_LONG).show()
                        rollNumber -> Toast.makeText(this, "Fill In The Roll Number Field",Toast.LENGTH_LONG).show()
                        year -> Toast.makeText(this, "Fill In The Year Field",Toast.LENGTH_LONG).show()
                        department -> Toast.makeText(this, "Fill In The Department Field",Toast.LENGTH_LONG).show()
                        studentIdNumber -> Toast.makeText(this, "Fill In The ID Number Field",Toast.LENGTH_LONG).show()
                    }
                }
            }

            if (!password.contentEquals(passwordConfirm))
                Toast.makeText(this, "Passwords don't match",Toast.LENGTH_LONG)
            else
                signUp(name, email, password, rollNumber.toInt(), studentIdNumber.toInt(), department, year.toInt(), isStudent, isFaculty)
        }

    }

    private fun signUp(name: String, email: String, password: String, rollNumber: Int?, studentIdNumber: Int?, department: String?, year: Int?, isStudent: Boolean?, isFaculty: Boolean?) {
        //Logic for Signing up User
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home

                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!, rollNumber, studentIdNumber, department, year, isStudent, isFaculty)
                    val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpActivity, "Some error occurred", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserToDatabase(name: String?, email: String?, uID: String?, rollNumber: Int?, studentIdNumber: Int?, department: String?, year: Int?, isStudent: Boolean?, isFaculty: Boolean?) {
        //Initialize DB Reference
        mDBReference = FirebaseDatabase.getInstance().getReference()

        //"user" is parent node in path
        mDBReference.child("user").child(uID!!).setValue(User(name, email, uID, rollNumber, studentIdNumber, department, year, isStudent, isFaculty))

    }

    // Checkboxes determine if newly created user is student, faculty, or both
    fun onCheckboxClicked(view: android.view.View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkbox_student -> {
                    isStudent = checked
                }
                R.id.checkbox_faculty -> {
                    isFaculty = checked
                }
            }
        }
    }
}