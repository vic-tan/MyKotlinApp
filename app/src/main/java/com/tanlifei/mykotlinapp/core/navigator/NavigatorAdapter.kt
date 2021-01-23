package com.tanlifei.mykotlinapp.core.navigator

import androidx.fragment.app.Fragment
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 18:03
 */
class NavigatorAdapter(fragments: MutableList<Fragment>) : INavigatorAdapter {

    var mFragments: MutableList<Fragment> = fragments

    override fun onCreateFragment(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getTag(position: Int): String {
        return mFragments[position].javaClass.simpleName
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}