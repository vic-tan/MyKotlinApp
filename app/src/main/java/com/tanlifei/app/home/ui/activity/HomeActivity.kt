package com.tanlifei.app.home.ui.activity


import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.navigator.NavigatorView
import com.common.core.base.ui.activity.BaseBVMActivity
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.ComUtils
import com.common.utils.ResUtils
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityHomeBinding
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.main.viewmodel.UpdateAppViewModel
import constant.UiType
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils


/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 16:07
 */
open class HomeActivity : BaseBVMActivity<ActivityHomeBinding, HomeViewModel>(),
    NavigatorView.NavigatorListener {

    lateinit var updateAppViewModel: UpdateAppViewModel

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(HomeActivity::class.java)
        }
    }

    override fun createViewModel(): HomeViewModel {
        return HomeViewModel()
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
        updateAppViewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(UpdateAppViewModel())
        ).get(
            UpdateAppViewModel::class.java
        )
        viewModel.bindFragments()
        viewModel.initNavigator(supportFragmentManager)
        viewModel.requestUser()
        updateAppViewModel.requestVersion()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.currTabPosition.observe(this, Observer {
            binding.navigatorTab.select(it)
        })

        updateAppViewModel.updateApp.observe(this, Observer {
            ComUtils.udpateApp(it)
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
        viewModel.showFragment(position)
    }


    override fun onBackPressed() {
        ComUtils.exitApp()
    }


}