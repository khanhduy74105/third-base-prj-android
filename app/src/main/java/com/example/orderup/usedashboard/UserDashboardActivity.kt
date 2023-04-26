package com.example.orderup.usedashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.LoginActivity
import com.example.orderup.R
import com.example.orderup.databinding.ActivityUserDashboardBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModalUser
import com.example.orderup.welcome.WelcomeActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDashboardBinding
    private lateinit var header: View
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle;
    private lateinit var toolbar: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    var provider: String?  = ""
    var currentUser: ModalUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provider = intent.getStringExtra("provider")
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        if (provider == "facebook") {

            tool.getFacebookUser { user ->
                if (user != null) {
                    currentUser = user
                    Log.i("GG", user.profileImage)
                    initUI(user);
                } else {
                    Log.i("GG", "user.profileImage")

                }
            }
            return
        } else {
            initUI(null);
        }


    }

    private fun initUI(user: ModalUser?) {
        Log.i("GG", "name facebooak: ${user?.username}")
        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav

        val viewPagerAdapter = ViewPageAdapter(
            fragmentManager = supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = viewPagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tag_home -> {
                    viewPager.currentItem = 0
                }
                R.id.tag_cart -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        viewPager.currentItem = 1
                    }
                }
                R.id.tag_favorite -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        viewPager.currentItem = 2
                    }
                }
                R.id.tag_order -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        viewPager.currentItem = 3
                    }
                }
                R.id.tag_profile -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        viewPager.currentItem = 4
                    }
                }
            }
            true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_home).isChecked = true
                        navigationView.menu.findItem(R.id.nav_home).isChecked = true
                    }
                    1 -> {
                        if (firebaseAuth.uid == null && user == null) {
                            startActivity(
                                Intent(
                                    this@UserDashboardActivity,
                                    LoginActivity::class.java
                                )
                            )
                        } else {
                            bottomNavigationView.menu.findItem(R.id.tag_cart).isChecked = true
                            navigationView.menu.findItem(R.id.nav_myCart).isChecked = true
                        }

                    }
                    2 -> {
                        if (firebaseAuth.uid == null && user == null) {
                            startActivity(
                                Intent(
                                    this@UserDashboardActivity,
                                    LoginActivity::class.java
                                )
                            )
                        } else {
                            bottomNavigationView.menu.findItem(R.id.tag_favorite).isChecked = true
                            navigationView.menu.findItem(R.id.nav_favorite).isChecked = true
                        }

                    }
                    4 -> {
                        if (firebaseAuth.uid == null && user == null) {
                            startActivity(
                                Intent(
                                    this@UserDashboardActivity,
                                    LoginActivity::class.java
                                )
                            )
                        } else {
                            bottomNavigationView.menu.findItem(R.id.tag_profile).isChecked = true
                            navigationView.menu.findItem(R.id.nav_profile).isChecked = true
                        }


                    }
                    3 -> {
                        if (firebaseAuth.uid == null && user == null) {
                            startActivity(
                                Intent(
                                    this@UserDashboardActivity,
                                    LoginActivity::class.java
                                )
                            )
                        } else {
                            bottomNavigationView.menu.findItem(R.id.tag_order).isChecked = true
                            navigationView.menu.findItem(R.id.nav_order).isChecked = true
                        }


                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        }
        )

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.toolbar
//        setSupportActionBar(toolbar)
        navigationView.bringToFront()
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        toolbar.setOnClickListener {
            drawerLayout.openDrawer(Gravity.RIGHT)
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {

                    viewPager.currentItem = 0
                    true
                }
                R.id.nav_myCart -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this@UserDashboardActivity, LoginActivity::class.java))
                        false
                    } else {
                        viewPager.currentItem = 1
                        true
                    }
                }
                R.id.nav_favorite -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this@UserDashboardActivity, LoginActivity::class.java))
                        false
                    } else {
                        viewPager.currentItem = 2
                        true
                    }
                }
                R.id.nav_order -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this@UserDashboardActivity, LoginActivity::class.java))
                        false
                    } else {
                        viewPager.currentItem = 3
                        true
                    }
                }
                R.id.nav_profile -> {
                    if (firebaseAuth.uid == null && user == null) {
                        startActivity(Intent(this@UserDashboardActivity, LoginActivity::class.java))
                        false
                    } else {
                        viewPager.currentItem = 4
                        true
                    }
                }
                R.id.nav_logout -> {
                    if (provider == "facebook"){
                        LoginManager.getInstance().logOut()
                        firebaseAuth.signOut()
                        AccessToken.refreshCurrentAccessTokenAsync()
                    }else{
                        firebaseAuth.signOut()
                        CookieManager.getInstance().removeAllCookies(null);
                        WebStorage.getInstance().deleteAllData();
                    }
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    true
                }
                R.id.nav_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
        header = navigationView.getHeaderView(0)
        checkUser()

    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null && currentUser == null) {
            val userIv = header.findViewById<ImageView>(R.id.userIv)
            userIv.setImageResource(R.drawable.logo_transparent)
            header.findViewById<TextView>(R.id.userNameTv).text = "Fill your stomach"
            header.findViewById<TextView>(R.id.userEmailTv).visibility = View.INVISIBLE
            navigationView.menu.findItem(R.id.nav_logout).isVisible = false
            navigationView.menu.findItem(R.id.nav_profile).isVisible = false
            navigationView.menu.findItem(R.id.nav_login).isVisible = true
            return
        } else {
            var uid:String? = null
            if (provider == "facebook"){
                uid = currentUser?.uid
            }else{
                uid = firebaseAuth.currentUser?.uid
            }

            if (uid != null) {
                val ref = FirebaseDatabase.getInstance().getReference("Users")
                navigationView.menu.findItem(R.id.nav_profile).isVisible = true
                navigationView.menu.findItem(R.id.nav_logout).isVisible = true
                navigationView.menu.findItem(R.id.nav_login).isVisible = false
                header.findViewById<TextView>(R.id.userEmailTv).visibility = View.VISIBLE
                ref.child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            header.findViewById<TextView>(R.id.userNameTv).text =
                                snapshot.child("username").value.toString()
                            header.findViewById<TextView>(R.id.userEmailTv).text =
                                snapshot.child("email").value.toString()
                            val imageView = header.findViewById<ImageView>(R.id.userIv)
                            val imgUrl = snapshot.child("profileImage").value.toString()
                            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                            Glide.with(imageView.context)
                                .load(imgUri)
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.ic_broken_image))
                                .into(imageView)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }

        }


    }

}