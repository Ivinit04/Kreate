package com.example.myapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Forgot_Password : AppCompatActivity() {


    private lateinit var edtPassword: EditText
    private lateinit var btnResetPassword: Button

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        btnResetPassword = findViewById(R.id.Reset_Pass)
        edtPassword = findViewById(R.id.reset_email)


        auth = FirebaseAuth.getInstance()

        btnResetPassword.setOnClickListener {
            val sPassword = edtPassword.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener{
                    Toast.makeText(this,"Please Check Your Email" , Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}