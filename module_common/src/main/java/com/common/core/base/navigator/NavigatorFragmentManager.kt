package com.common.core.base.navigator

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.FragmentUtils

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 16:40
 */
open class NavigatorFragmentManager(
    fragmentManager: FragmentManager,
    adapter: INavigatorAdapter,
    @IdRes containerViewId: Int
) {

    private var mFragmentManager: FragmentManager = fragmentManager

    private var mAdapter: INavigatorAdapter = adapter

    private var mContainerViewId: Int = containerViewId

    private var mCurrentPosition: Int = -1


    open fun showFragment(position: Int) {
        mCurrentPosition = position
        val count = mAdapter!!.getCount()
        for (i in 0 until count) {
            if (position == i) {
                show(i)
            } else {
                hide(i)
            }
        }
    }


    open fun show(position: Int) {
        val tag = mAdapter.getTag(position)
        val fragment = mFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            add(position)
        } else {
            FragmentUtils.show(fragment)
        }
    }

    open fun hide(position: Int) {
        val tag = mAdapter.getTag(position)
        val fragment = mFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            FragmentUtils.hide(fragment)
        }
    }


    open fun add(position: Int) {
        var fragment = mAdapter.onCreateFragment(position)
        val tag = mAdapter.getTag(position)

        if (fragment != null) {
            FragmentUtils.add(mFragmentManager, fragment, mContainerViewId, tag)
        }

    }
}





