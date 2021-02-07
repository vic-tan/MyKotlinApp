package com.common.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 13:12
 */
class ComViewPagerActAdapter (fm: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}