package com.tanlifei.app.home.ui.activity


import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseActivity
import com.hjq.toast.ToastUtils
import com.tanlifei.app.R
import com.common.base.navigator.NavigatorAdapter
import com.common.base.navigator.NavigatorFragmentManager
import com.common.base.navigator.NavigatorView
import com.tanlifei.app.common.utils.AppUtils
import com.tanlifei.app.databinding.ActivityHomeBinding
import com.tanlifei.app.home.ui.fragment.*
import com.tanlifei.app.main.ui.LoginAtivity
import java.util.*


/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 16:07
 */
open class HomeActivity : BaseActivity<ActivityHomeBinding>(), NavigatorView.NavigatorListener {

    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mNavigator: NavigatorFragmentManager
    var mCurrTabPosition: Int = 0 //当前选中tag

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(HomeActivity::class.java)
        }
    }

    override fun layoutResId(): Int {
        return R.layout.activity_home
    }

    override fun createBinding(layoutView: View): ActivityHomeBinding {
        return ActivityHomeBinding.bind(layoutView)
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
        defaultSelectNavTabPosition()
        if (binding.navigatorTab != null) {
            binding.navigatorTab.setNavigatorListener(this)
        }
    }

    /**
     * 设置默认选择TAB， 第一次时显示
     */
    private fun defaultSelectNavTabPosition() {
        mNavigator.showFragment(mCurrTabPosition) //显示点击Fargment
        binding.navigatorTab.select(mCurrTabPosition)
        binding.navigatorTab.getMsgBadge().visibility = View.VISIBLE
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
        binding.navigatorTab.select(mCurrTabPosition)
        mNavigator.showFragment(mCurrTabPosition) //显示点击Fargment

    }

    override fun onBackPressed() {
        AppUtils.exitApp()
    }


}