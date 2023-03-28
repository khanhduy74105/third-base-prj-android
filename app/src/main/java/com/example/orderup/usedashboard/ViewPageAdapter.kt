package com.example.orderup.usedashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPageAdapter(fragmentManager: FragmentManager, behavior: Int): FragmentStatePagerAdapter(fragmentManager) {


    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return HomeFragment()
            }
            1 -> {
                return CartFragment()
            }
            2 -> {
                return FavoriteFragment()
            }
            3 -> {
                return UserFragment()
            }
            else -> {
                return HomeFragment()
            }
        }
    }

}