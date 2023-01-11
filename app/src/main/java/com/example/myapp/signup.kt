package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.pass
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button

    private lateinit var auth: FirebaseAuth
    private var mIsShowPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtEmail = findViewById(R.id.email)
        edtPhoneNumber = findViewById(R.id.phone_No)
        edtPassword = findViewById(R.id.pass)
        btnSignup = findViewById(R.id.btn_signUp)

        auth = Firebase.auth

        eye1.setOnClickListener{
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)

        eye2.setOnClickListener {
            mIsShowPass = !mIsShowPass
            showPass(mIsShowPass)
        }
        showPass(mIsShowPass)

        btnSignup.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }


        }
    }

    private fun showPass(isShow: Boolean) {
        if(isShow){
            cnf_pass.transformationMethod  = HideReturnsTransformationMethod.getInstance()
            eye2.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        else{
            cnf_pass.transformationMethod  = PasswordTransformationMethod.getInstance()
            eye2.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        pass.setSelection(pass.text.toString().length)
    }

    private fun showPassword(isShow: Boolean) {
        if(isShow){
            pass.transformationMethod  = HideReturnsTransformationMethod.getInstance()
            eye1.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        else{
            pass.transformationMethod  = PasswordTransformationMethod.getInstance()
            eye1.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        pass.setSelection(pass.text.toString().length)
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this , Home_Page::class.java)
        startActivity(intent)
    }
}