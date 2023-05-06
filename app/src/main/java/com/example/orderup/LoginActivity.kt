package com.example.orderup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.orderup.databinding.ActivityLoginBinding
import com.example.orderup.lib.tool
import com.example.orderup.restaurantdashboard.RestaurantDashboardActivity
import com.example.orderup.usedashboard.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog


    val TAG = "LOGIN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(true)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, UserDashboardActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            validateData()
        }

        binding.google.setOnClickListener {
            startActivity(Intent(this, GoogleAuthActivity::class.java))
        }

        binding.facebook.setOnClickListener {
            startActivity(Intent(this, FacebookActivity::class.java))
        }
    }
    private var email = ""
    private var password = ""
    private fun validateData(){
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
        }else{
            loginUser()
        }
    }
    private fun loginUser(){
        progressDialog.setMessage("Logging in")
        progressDialog.show()
        Log.i(TAG,"loginuser")
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Log.i(TAG,"dang ki that bai ${it.message}")
                Toast.makeText(this, "Login faileure due to ${it.message}",Toast.LENGTH_SHORT)
            }
    }

    private fun checkUser() {
        progressDialog.setMessage("Checking user")
        val uid = firebaseAuth.currentUser!!.uid
        Log.i(TAG,"$uid")
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    tool.setCurrentId(uid)
                    val userType = snapshot.child("userType").value
                    if (userType =="user"){
                        startActivity(Intent(this@LoginActivity, UserDashboardActivity::class.java))
                        finish()
                    }
                    else if (userType == "owner") {
                        startActivity(Intent(this@LoginActivity, RestaurantDashboardActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@LoginActivity, UserDashboardActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}