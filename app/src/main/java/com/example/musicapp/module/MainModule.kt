package com.example.musicapp.module

import androidx.fragment.app.FragmentActivity
import com.example.musicapp.adapter.ViewPagerAdapter
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun getViewPagerAdapter(fragmentActivity: FragmentActivity) : ViewPagerAdapter{
        return ViewPagerAdapter(fragmentActivity)
    }


}