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
import kotlinx.android.synthetic.main.activity_login.pass
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {

    private lateinit var edtPhoneNo: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtCnfPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnGoogle: ImageButton
    private lateinit var btnFacebook: ImageButton


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var mIsShowPass = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtPhoneNo = findViewById(R.id.phoneNo)
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.pass)
        edtCnfPassword = findViewById(R.id.cnf_pass)
        btnSignup = findViewById(R.id.btn_signUp)
        btnGoogle = findViewById(R.id.btn_google)
        btnFacebook = findViewById(R.id.btn_facebook)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)
        btnGoogle.setOnClickListener {
            signInGoogle()
        }

        pass_eye.setOnClickListener{
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)

        cnfPass_eye.setOnClickListener {
            mIsShowPass = !mIsShowPass
            showPass(mIsShowPass)
        }
        showPass(mIsShowPass)

        btnSignup.setOnClickListener {

            val phoneNumber = edtPhoneNo.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val cnfPassword = edtCnfPassword.text.toString().trim()

            if (phoneNumber.isEmpty()){
                edtPhoneNo.error = "Phone Number Is Required"
                return@setOnClickListener
            }else if (email.isEmpty() || !email.contains("@gmail.com")){
                edtEmail.error = "Correct Email Is Required"
                return@setOnClickListener
            }else if (password.isEmpty()){
                edtPassword.error = "Password Is Required"
                return@setOnClickListener
            }else if (cnfPassword.isEmpty()){
                edtCnfPassword.error = "Password Is Not Confirmed"
                return@setOnClickListener
            }else if (cnfPassword != password){
                edtCnfPassword.error = "Password Is Incorrect"
            }
            else{
                auth.createUserWithEmailAndPassword(email, cnfPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "signup failed",
                                Toast.LENGTH_SHORT).show()
//                      updateUI(null)
                        }
                    }
            }


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


    private fun showPass(isShow: Boolean) {
        if(isShow){
            cnf_pass.transformationMethod  =HideReturnsTransformationMethod.getInstance()
            cnfPass_eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        else{
            cnf_pass.transformationMethod  =PasswordTransformationMethod.getInstance()
            cnfPass_eye.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        cnf_pass.setSelection(pass.text.toString().length)
    }

    private fun showPassword(isShow: Boolean) {
        if(isShow){
            pass.transformationMethod  =HideReturnsTransformationMethod.getInstance()
            pass_eye.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
        else{
            pass.transformationMethod  =PasswordTransformationMethod.getInstance()
            pass_eye.setImageResource(R.drawable.ic_baseline_visibility_24)
        }
        pass.setSelection(pass.text.toString().length)
    }


    private fun updateUI(user: FirebaseUser?) {

        val intent = Intent(this , Home_Page::class.java)
        startActivity(intent)
        finish()
    }
}