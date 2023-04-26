package com.example.orderup.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orderup.GoogleAuthActivity
import com.example.orderup.LoginActivity
import com.example.orderup.SignUpActivity
import com.example.orderup.databinding.ActivityWelcomeBinding
import com.example.orderup.lib.tool
import com.example.orderup.restaurantdashboard.RestaurantDashboardActivity
import com.example.orderup.usedashboard.UserDashboardActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.util.*


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {

                    val accessToken = AccessToken.getCurrentAccessToken()

                    val request = GraphRequest.newMeRequest(
                        accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(
                                obj: JSONObject?,
                                response: GraphResponse?
                            ) {
                                val uid = obj?.getString("id").toString()
                                Log.i("UID", "uid : $uid")
                                tool.setCurrentId(uid)

                                val ref = FirebaseDatabase.getInstance().getReference("Users")
                                ref.child(uid)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.value == null) {
                                                val uid: String = obj?.getString("id").toString()
                                                createNewAccount(obj, uid)
                                            }


                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.i("GG", "Khong co tai khoan")

                                        }
                                    })

                                val intent = (Intent(
                                    this@WelcomeActivity,
                                    UserDashboardActivity::class.java
                                ))
                                intent.putExtra("provider", "facebook")
                                intent.putExtra("uid", uid)
                                startActivity(intent)
                                finish()
                            }
                        })
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,link,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()


                }

                override fun onCancel() {
                    Log.i("RS", "cancel")

                }

                override fun onError(exception: FacebookException) {
                    Log.i("RS", "erroe $exception")

                }

            })

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        binding.skipBtn.setOnClickListener {
//            startActivity(Intent(this, UserDashboardActivity::class.java))
            startActivity(Intent(this, RestaurantDashboardActivity::class.java))
            finish()

        }
        binding.mormalBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.google.setOnClickListener {
            startActivity(Intent(this, GoogleAuthActivity::class.java))
        }

        binding.facebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(
                    this@WelcomeActivity,
                    listOf("email", "public_profile", "user_friends")
                );
        }
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun createNewAccount(obj: JSONObject?, uid: String) {
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = ""
        hashMap["username"] = obj?.getString("name").toString()
        hashMap["profileImage"] =
            obj?.getJSONObject("picture")?.getJSONObject("data")?.getString("url").toString()
        hashMap["userType"] = "user"
        hashMap["address"] = ""
        hashMap["timestamp"] = timestamp
        hashMap["provider"] = "facebook"
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid)
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
