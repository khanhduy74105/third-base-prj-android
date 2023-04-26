package com.example.orderup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.orderup.lib.tool
import com.example.orderup.usedashboard.UserDashboardActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.util.HashMap

class FacebookActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook)

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
                                    this@FacebookActivity,
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

        LoginManager.getInstance()
            .logInWithReadPermissions(
                this@FacebookActivity,
                listOf("email", "public_profile", "user_friends")
            );
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