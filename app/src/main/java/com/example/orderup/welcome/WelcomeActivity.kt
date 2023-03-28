package com.example.orderup.welcome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.orderup.LoginActivity
import com.example.orderup.R
import com.example.orderup.SignUpActivity
import com.example.orderup.databinding.ActivityWelcomeBinding
import com.example.orderup.usedashboard.UserDashboardActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        binding.skipBtn.setOnClickListener {
            startActivity(Intent(this, UserDashboardActivity::class.java))
        }

        binding.mormalBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        setContentView(binding.root)
    }
}