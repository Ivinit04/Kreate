package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.pass)
        btnLogin = findViewById(R.id.btn_logIn)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }


    }
}