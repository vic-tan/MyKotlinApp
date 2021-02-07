package com.common.core.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @desc:Viewpage 2 在Fragment 中用该类
 * @author: tanlifei
 * @date: 2021/2/7 13:11
 */
class ComFragPagerAdapter(fm: Fragment, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}