package com.example.orderup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.orderup.lib.tool
import com.example.orderup.usedashboard.UserDashboardActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase

class GoogleAuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_auth)

        // initialize variable auth to conduct authentication
        auth = Firebase.auth

        // Initialize sign in options the client-id is copied form google-services.json file
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signIntent = googleSignInClient.signInIntent
        this@GoogleAuthActivity.startActivityForResult(signIntent, 120)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            val signInAccountTask: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data!!)
            if (signInAccountTask.isSuccessful) {
                try {


//

                    val googleSignInAccount = signInAccountTask.getResult(ApiException::class.java)
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken, null
                        )
                        // Check credential
                        auth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task ->
                                // Check condition
                                if (task.isSuccessful) {
                                    Log.i("GG", "success")
                                    val user = FirebaseAuth.getInstance().currentUser
                                    if (user?.uid != null) {
                                        val ref =
                                            FirebaseDatabase.getInstance().getReference("Users")
                                        ref.child(user.uid)
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.value == null) {
                                                        createNewAccount()
                                                    }
                                                    tool.setCurrentId(user.uid)
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Log.i("GG", "Khong co tai khoan")

                                                }

                                            })
                                    }
                                    startActivity(Intent(this, UserDashboardActivity::class.java))
                                    finish()
                                } else {
                                    Log.i("GG", "no success")
                                }
                            }
                            .addOnSuccessListener {
                                Log.i("GG", "success on")
                            }
                            .addOnFailureListener {
                                Log.i("GG", "failure")

                            }
                    } else {
                        Log.i("GG", "googleSignInAccount null")
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            } else {
                Log.i("GG", "signInAccountTask failure")
            }
        }
    }


    fun createNewAccount() {
        val timestamp = System.currentTimeMillis()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val hashMap: HashMap<String, Any?> = HashMap()
            hashMap["uid"] = user.uid
            hashMap["email"] = user.email
            hashMap["username"] = user.displayName
            hashMap["profileImage"] = user.photoUrl.toString()
            hashMap["userType"] = "user"
            hashMap["address"] = ""
            hashMap["timestamp"] = timestamp
            hashMap["provider"] = true
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(user.uid)
                .setValue(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "creating account successfully", Toast.LENGTH_SHORT).show()
                    finish()
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Failed creating account due to ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }
}