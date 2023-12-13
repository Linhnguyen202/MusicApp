package com.example.musicapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.layout.HomeScreen
import com.example.musicapp.layout.MenuScreen
import com.example.musicapp.layout.ProfileScreen
import com.example.musicapp.layout.SearchScreen
import javax.inject.Inject

class ViewPagerAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    override fun getItemCount(): Int {
        return 4;
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return HomeScreen()
            }
            1 -> {
                return SearchScreen()
            }
            2 -> {
                return MenuScreen()
            }
            3 -> {
                return ProfileScreen()
            }
            else -> {
                return HomeScreen()
            }
        }
    }
}