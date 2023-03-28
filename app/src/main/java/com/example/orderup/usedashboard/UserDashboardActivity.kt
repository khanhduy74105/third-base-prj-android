package com.example.orderup.usedashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.orderup.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        initUI()
    }

    private fun initUI() {
        viewPager = this.findViewById(R.id.view_pager)
        bottomNavigationView = this.findViewById(R.id.bottomNav)

        var viewPagerAdapter = ViewPageAdapter(fragmentManager = supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager.adapter = viewPagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.tag_home -> {
                    viewPager.setCurrentItem(0)
                }
                R.id.tag_cart -> {
                    viewPager.setCurrentItem(1)
                }
                R.id.tag_favorite -> {
                    viewPager.setCurrentItem(2)
                }
                R.id.tag_profile -> {
                    viewPager.setCurrentItem(3)
                }
            }
            true
        }

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 ->{
                        bottomNavigationView.menu.findItem(R.id.tag_home).setChecked(true)
                    }
                    1 ->{
                        bottomNavigationView.menu.findItem(R.id.tag_cart).setChecked(true)
                    }
                    2 ->{
                        bottomNavigationView.menu.findItem(R.id.tag_favorite).setChecked(true)
                    }
                    3 ->{
                        bottomNavigationView.menu.findItem(R.id.tag_profile).setChecked(true)
                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        }
        )
    }
}