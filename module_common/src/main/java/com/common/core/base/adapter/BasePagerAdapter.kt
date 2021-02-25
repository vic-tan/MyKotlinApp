package com.common.core.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @desc:Viewpage
 * @author: tanlifei
 * @date: 2021/2/7 13:11
 */
class BasePagerAdapter(manager: FragmentManager, private val fragments: List<Fragment>) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}