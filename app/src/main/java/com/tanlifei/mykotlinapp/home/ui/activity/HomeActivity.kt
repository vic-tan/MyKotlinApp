package com.tanlifei.mykotlinapp.home.ui.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.core.navigator.NavigatorView
import com.tanlifei.mykotlinapp.core.navigator.NavigatorFragmentManager
import com.tanlifei.mykotlinapp.core.navigator.NavigatorAdapter
import com.tanlifei.mykotlinapp.home.ui.fragment.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 16:07
 */
open class HomeActivity : BaseActivity(), NavigatorView.NavigatorListener {
    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mNavigator: NavigatorFragmentManager
    var mCurrTabPosition: Int = 0 //当前选中tag


    override fun layoutResId(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        bindFragments()
        initNavigator()
    }


    /**
     * 初始化 Navigator
     */
    open fun initNavigator() {
        mNavigator = NavigatorFragmentManager(
            supportFragmentManager,
            NavigatorAdapter(mFragments),
            R.id.container
        )
        if (navigatorTab != null) {
            navigatorTab.setNavigatorListener(this)
        }
        mNavigator.showFragment(mCurrTabPosition) //显示点击Fargment
        navigatorTab.select(mCurrTabPosition)
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    open fun bindFragments() {
        mFragments.add(HomeFragment.newInstance())
        mFragments.add(ClassFragment.newInstance())
        mFragments.add(ClassmateCircleFragment.newInstance())
        mFragments.add(StudyFragment.newInstance())
        mFragments.add(PersenalFragment.newInstance())
    }

    override fun onNavigatorItemClick(position: Int, view: View?) {
        mCurrTabPosition = position
        navigatorTab.select(mCurrTabPosition)
        mNavigator.showFragment(mCurrTabPosition) //显示点击Fargment

    }


}