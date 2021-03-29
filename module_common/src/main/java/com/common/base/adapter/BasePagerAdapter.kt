package com.common.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @desc:Viewpage
 * @author: tanlifei
 * @date: 2021/2/7 13:11
 */
class BasePagerAdapter(mManager: FragmentManager, private val mFragments: List<Fragment>) :
    FragmentPagerAdapter(mManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}