package com.tanlifei.app.main.ui.activity


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.event.BaseEvent
import com.common.core.base.navigator.NavigatorView
import com.common.core.base.ui.activity.BaseBVMActivity
import com.common.core.base.viewmodel.BaseViewModel
import com.common.core.http.RxHttpManager
import com.common.utils.ComUtils
import com.common.utils.extension.setVisible
import com.tanlifei.app.common.event.UserEvent
import com.tanlifei.app.databinding.ActivityMainBinding
import com.tanlifei.app.main.viewmodel.MainViewModel
import com.tanlifei.app.main.viewmodel.UpdateAppViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 16:07
 */
open class MainActivity : BaseBVMActivity<ActivityMainBinding, MainViewModel>(),
    NavigatorView.NavigatorListener {

    lateinit var updateAppViewModel: UpdateAppViewModel

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(MainActivity::class.java)
        }
    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel()
    }


    override fun init() {
        RxHttpManager.addToken()
        initViewModel()
        initViewModelObserve()
        initListener()
        binding.navigatorTab.getMsgBadge().setVisible(true)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: BaseEvent) {
        if (event is UserEvent) {
            viewModel.requestUser()
        }
    }

    override fun registerEventBus(): Boolean {
        return true
    }

    override fun onNavigatorItemClick(position: Int, view: View?) {
        viewModel.showFragment(position)
    }


    override fun onBackPressed() {
        ComUtils.exitApp()
    }


}