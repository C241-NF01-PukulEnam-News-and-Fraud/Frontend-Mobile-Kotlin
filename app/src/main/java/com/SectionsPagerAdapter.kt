package com

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pukul6.fragment.FraudFragment
import com.example.pukul6.fragment.HistoryFragment
import com.example.pukul6.fragment.NewsFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return  3
    }
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = NewsFragment()
            1 -> fragment = FraudFragment()
            2 -> fragment = HistoryFragment()
        }
        return fragment as Fragment
    }

}