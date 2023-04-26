package com.example.orderup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.orderup.databinding.ActivitySignUpBinding
import com.example.orderup.usedashboard.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    val TAG = "SIGN_UP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        init
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.signUpBtn.setOnClickListener {
            verifyData()
        }
    }
    var username: String = ""
    var email: String = ""
    var password: String = ""
    private fun verifyData() {
        username = binding.userNameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        val cPassword = binding.cfPasswordEt.text.toString().trim()
        if (username.isEmpty()){
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
        }else if (cPassword.isEmpty()){
            Toast.makeText(this, "Confirm password", Toast.LENGTH_SHORT).show()
        }else if (password != cPassword){
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
        }else{
            Log.i(TAG, "create account")
            createAccount()
        }
    }

    private fun createAccount() {
        progressDialog.setMessage("Creating account")
        progressDialog.show()

//        create user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
//                account create
                Log.i(TAG, "creat success")
                updateUserInfo()
            }
            .addOnFailureListener { it ->
                progressDialog.dismiss()
                Log.i(TAG, "create failed")
                Toast.makeText(this, "Failed creating account due to ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateUserInfo() {
        progressDialog.setMessage("Saving....")
        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid!!
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["uid"] = uid
        hashMap["email"]=email
        hashMap["username"]=username
        hashMap["profileImage"] = "" //empty add in edit
        hashMap["userType"] = "user"
        hashMap["address"] = ""
        hashMap["timestamp"] = timestamp
        Log.i(TAG, "${hashMap}}")
        Log.i(TAG, "$uid")

        // set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener {
                    Log.i(TAG, "luu vao DB success")
                    progressDialog.dismiss()
                    Toast.makeText(this, "creating account successfully", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                }
                .addOnFailureListener {
                    Log.i(TAG, "luu vao DB that bai")
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed creating account due to ${it.message}", Toast.LENGTH_SHORT).show()
                }

    }
}