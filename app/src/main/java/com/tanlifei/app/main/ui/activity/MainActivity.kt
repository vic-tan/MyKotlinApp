package com.tanlifei.app.main.ui.activity


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.common.core.event.BaseEvent
import com.common.core.navigator.NavigatorView
import com.common.base.ui.activity.BaseActivity
import com.common.base.viewmodel.BaseViewModel
import com.common.core.http.RxHttpManager
import com.common.utils.ComUtils
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.startActivity
import com.tanlifei.app.common.constant.EnumConst
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
open class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavigatorView.NavigatorListener {

    lateinit var mUpdateAppViewModel: UpdateAppViewModel

    companion object {
        fun actionStart() {
            startActivity<MainActivity> { }
        }
    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun onResume() {
        super.onResume()
        //当前选中为同学圈，且同学圈选择关注Tab时，自动播放视频
        if (mViewModel.mHomeCurrentTabPosition == EnumConst.HomeTabTag.CIRCLE.value
            && mViewModel.mCircleCurrentTabPosition == EnumConst.CircleTabTag.CIRCLE.value
        ) {
            mViewModel.postLiveDataRecommendPageFragment(mViewModel.mCircleCurrentTabPosition)
        }
    }


    override fun init() {
        RxHttpManager.addToken()
        initViewModel()
        initViewModelObserve()
        initListener()
        mBinding.navigatorTab.getMsgBadge().setVisible(true)
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        mUpdateAppViewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(UpdateAppViewModel())
        ).get(
            UpdateAppViewModel::class.java
        )
        mViewModel.bindFragments()
        mViewModel.initNavigator(supportFragmentManager)
        mViewModel.requestUser()
        mUpdateAppViewModel.requestVersion()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mCurrentTabPosition.observe(this, Observer {
            mBinding.navigatorTab.select(it)
            when (it) {
                EnumConst.HomeTabTag.CIRCLE.value -> {
                    mViewModel.postLiveDataRecommendPageFragment(mViewModel.mCircleCurrentTabPosition)
                }
                else -> {
                    mViewModel.postLiveDataRecommendPageFragment(EnumConst.CircleTabTag.RECOMMEND.value)
                }
            }
        })

        mUpdateAppViewModel.mUpdateApp.observe(this, Observer {
            ComUtils.udpateApp(it)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        if (mBinding.navigatorTab != null) {
            mBinding.navigatorTab.setNavigatorListener(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: BaseEvent) {
        if (event is UserEvent) {
            mViewModel.requestUser()
        }
    }

    override fun registerEventBus(): Boolean {
        return true
    }

    override fun onNavigatorItemClick(position: Int, view: View?) {
        mViewModel.showFragment(position)
    }


    override fun onBackPressed() {
        ComUtils.exitApp()
    }


}