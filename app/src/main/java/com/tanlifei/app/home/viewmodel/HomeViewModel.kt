package com.tanlifei.app.home.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.core.base.navigator.NavigatorAdapter
import com.common.core.base.navigator.NavigatorFragmentManager
import com.tanlifei.app.R
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.home.network.HomeNetwork
import com.tanlifei.app.home.ui.fragment.ClassFragment
import com.tanlifei.app.home.ui.fragment.ClassmateCircleFragment
import com.tanlifei.app.home.ui.fragment.HomeFragment
import com.tanlifei.app.home.ui.fragment.StudyFragment
import com.tanlifei.app.persenal.fragment.PersonalFragment
import java.util.ArrayList

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/4 15:01
 */
class HomeViewModel(private val repository: HomeNetwork) : BaseViewModel() {

    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mNavigator: NavigatorFragmentManager

    /**
     * 点击TAB时LveData
     */
    val currTabPosition: LiveData<Int> get() = _currTabPosition
    private val _currTabPosition = MutableLiveData<Int>()

    init {
        _currTabPosition.value = 0
    }

    /**
     * 初始化 Navigator
     */
    fun initNavigator(fragmentManager: FragmentManager) {
        mNavigator = NavigatorFragmentManager(
            fragmentManager,
            NavigatorAdapter(mFragments),
            R.id.tabContainer
        )
        showFragment(0)
    }


    fun showFragment(position: Int) {
        _currTabPosition.value = position
        mNavigator.showFragment(position) //显示点击Fargment
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    fun bindFragments() {
        mFragments.add(HomeFragment.newInstance())
        mFragments.add(ClassFragment.newInstance())
        mFragments.add(ClassmateCircleFragment.newInstance())
        mFragments.add(StudyFragment.newInstance())
        mFragments.add(PersonalFragment.newInstance())
    }
}