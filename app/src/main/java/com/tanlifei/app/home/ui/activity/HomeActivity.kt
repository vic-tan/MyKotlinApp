package com.tanlifei.app.home.ui.activity


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.navigator.NavigatorView
import com.common.core.base.ui.activity.BaseActivity
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.AppUtils
import com.tanlifei.app.databinding.ActivityHomeBinding
import com.tanlifei.app.home.viewmodel.HomeViewModel


/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 16:07
 */
open class HomeActivity : BaseActivity<ActivityHomeBinding>(), NavigatorView.NavigatorListener {

    public lateinit var homeViewModel: HomeViewModel

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(HomeActivity::class.java)
        }
    }


    override fun init() {
        initViewModel()
        initViewModelObserve()
        initListener()
        binding.navigatorTab.getMsgBadge().visibility = View.VISIBLE

    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(HomeViewModel())
        ).get(
            HomeViewModel::class.java
        )
        homeViewModel.bindFragments()
        homeViewModel.initNavigator(supportFragmentManager)
        homeViewModel.requestUser()
        homeViewModel.requestVersion()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        homeViewModel.currTabPosition.observe(this, Observer {
            binding.navigatorTab.select(it)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        if (binding.navigatorTab != null) {
            binding.navigatorTab.setNavigatorListener(this)
        }
    }


    override fun onNavigatorItemClick(position: Int, view: View?) {
        homeViewModel.showFragment(position)
    }


    override fun onBackPressed() {
        AppUtils.exitApp()
    }

}