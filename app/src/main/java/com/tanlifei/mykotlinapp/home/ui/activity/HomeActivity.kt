package com.tanlifei.mykotlinapp.home.ui.activity


import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.hjq.toast.ToastUtils
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.core.navigator.NavigatorAdapter
import com.tanlifei.mykotlinapp.core.navigator.NavigatorFragmentManager
import com.tanlifei.mykotlinapp.core.navigator.NavigatorView
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
    private var isExit = false

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
        defaultSelectNavTabPosition()
        if (navigatorTab != null) {
            navigatorTab.setNavigatorListener(this)
        }
    }

    /**
     * 设置默认选择TAB， 第一次时显示
     */
    private fun defaultSelectNavTabPosition() {
        mNavigator.showFragment(mCurrTabPosition) //显示点击Fargment
        navigatorTab.select(mCurrTabPosition)
        navigatorTab.getMsgBadge().visibility = View.VISIBLE
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

    override fun onBackPressed() {
        exitApp()
    }

    /**
     * 退出App
     */
    protected open fun exitApp() {
        var tExit: Timer? = null
        if (!isExit) {
            isExit = true // 准备退出
            ToastUtils.show(getString(R.string.app_exit))
            tExit = Timer()
            tExit.schedule(object : TimerTask() {
                override fun run() {
                    isExit = false // 取消退出
                }
            }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            //finish所有页面和kill app
            ActivityUtils.finishAllActivities()
        }
    }

}