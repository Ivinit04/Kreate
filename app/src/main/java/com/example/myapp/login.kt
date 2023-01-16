package com.example.myapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mIsShowPass = false

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnGoogle: ImageButton
    private lateinit var btnFacebook: ImageButton
    private lateinit var btnForgot: Button

    private lateinit var googleSignInClient: GoogleSignInClient


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.pass)
        btnLogin = findViewById(R.id.btn_logIn)
        btnRegister = findViewById(R.id.btn_register)
        btnGoogle = findViewById(R.id.btn_google)
        btnFacebook = findViewById(R.id.btn_facebook)
        btnForgot = findViewById(R.id.forgot_pass_btn)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)
        btnGoogle.setOnClickListener {
            signInGoogle()
        }

        //password Visibility
        eye.setOnClickListener{
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)

        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            if (email.isEmpty()){
                edtEmail.error = "Email Or PhoneNo Is Required"
                return@setOnClickListener
            }else if (password.isEmpty()){
                edtPassword.error = "Password Is Required"
                return@setOnClickListener
            }else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Email Or Password IS Incorrect",
                                Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                        }
                    }
            }

        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        btnForgot.setOnClickListener {
            val intent = Intent(this, Forgot_Password::class.java)
            startActivity(intent)
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }

    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }else {
                Toast.makeText(this , task.exception.toString() , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val intent = Intent(this , Home_Page::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showPassword(isShow: Boolean) {
        if(isShow){
            pass.transformationMethod  =HideReturnsTransformationMethod.getInstance()
            eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        else{
            pass.transformationMethod  =PasswordTransformationMethod.getInstance()
            eye.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        pass.setSelection(pass.text.toString().length)
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this , Home_Page::class.java)
        startActivity(intent)
        finish()
    }
}