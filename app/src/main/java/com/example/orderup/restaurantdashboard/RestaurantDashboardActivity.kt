package com.example.orderup.restaurantdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.orderup.LoginActivity
import com.example.orderup.R
import com.example.orderup.databinding.ActivityRestaurantDashboardBinding
import com.example.orderup.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class RestaurantDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDashboardBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: ImageButton
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private lateinit var header: View
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        viewPager = binding.viewResPager
        bottomNavigationView = binding.bottomNav

        val viewPagerAdapter = ViewPagerResAdapter(
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
                    viewPager.currentItem = 1
                }
                R.id.tag_favorite -> {
                    viewPager.currentItem = 2
                }
                R.id.tag_order -> {
                    viewPager.currentItem = 3
                }
                R.id.tag_profile -> {
                    viewPager.currentItem = 4
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
                        bottomNavigationView.menu.findItem(R.id.tag_cart).isChecked = true
                        navigationView.menu.findItem(R.id.nav_myCart).isChecked = true
                    }
                    2 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_favorite).isChecked = true
                        navigationView.menu.findItem(R.id.nav_favorite).isChecked = true
                    }
                    4 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_profile).isChecked = true
                        navigationView.menu.findItem(R.id.nav_money).isChecked = true
                    }
                    3 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_order).isChecked = true
                        navigationView.menu.findItem(R.id.nav_order).isChecked = true
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        }
        )
        navigationView = binding.navView
        toggle = ActionBarDrawerToggle(
            this,
            binding.homeRes,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        navigationView.bringToFront()
        binding.homeRes.addDrawerListener(toggle)
        toggle.syncState()
        toolbar = binding.toolbar
        toolbar.setOnClickListener {
            binding.homeRes.openDrawer(Gravity.LEFT)
        }
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.nav_myCart -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.nav_favorite -> {

                    viewPager.currentItem = 2
                    true
                }
                R.id.nav_order -> {
                    viewPager.currentItem = 3
                    true
                }
                R.id.nav_profile -> {
                    viewPager.currentItem = 4
                    true
                }
                R.id.nav_logout -> {
                    firebaseAuth.signOut()
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
    }
}