package com.example.orderup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.orderup.restaurantdashboard.RestaurantDashboardActivity
import com.example.orderup.usedashboard.UserDashboardActivity
import com.example.orderup.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        val user = FirebaseDatabase.getInstance().getReference("Users/$uid")

        Handler().postDelayed(Runnable {
            if (uid!= null){
                user.child("userType")
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value.toString() == "owner"){
                                startActivity(Intent(this@SplashActivity, RestaurantDashboardActivity::class.java))
                            }else if (snapshot.value.toString() == "user"){
                                startActivity(Intent(this@SplashActivity, UserDashboardActivity::class.java))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


            }else{
                startActivity(Intent(this,WelcomeActivity::class.java))
            }
        }, 2000)
    }
}