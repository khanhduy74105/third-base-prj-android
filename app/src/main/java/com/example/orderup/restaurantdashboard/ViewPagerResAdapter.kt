package com.example.orderup.restaurantdashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.orderup.restaurantdashboard.ui.AboutRestaurantFragment
import com.example.orderup.restaurantdashboard.ui.ManageFoodFragmentFragment
import com.example.orderup.usedashboard.*

class ViewPagerResAdapter (fragmentManager: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return AboutRestaurantFragment()
            }
            1 -> {
                return ManageOrder()
            }
            2 -> {
                return ManageCategory()
            }
            3 -> {
                return Chart()
            }
            4 ->{
                return ManageMoney()
            }
            else -> {
                return AboutRestaurantFragment()
            }
        }
    }

}