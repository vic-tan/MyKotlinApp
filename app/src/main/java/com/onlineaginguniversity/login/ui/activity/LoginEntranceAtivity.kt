package com.onlineaginguniversity.login.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseActivity
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.widget.component.extension.*
import com.onlineaginguniversity.databinding.ActivityLoginEntranceBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:登录方式（手机，微信方式登录）界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class LoginEntranceAtivity :
    BaseActivity<ActivityLoginEntranceBinding, LoginViewModel>() {


    companion object {
        fun actionStart() {
            startActivity<LoginEntranceAtivity> { }
            ActivityUtils.finishOtherActivities(LoginEntranceAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        LoginUtils.showProtocolTxt(mBinding.protocolTxt)
        LoginUtils.protocolCheck(mBinding.protocolCheckbox, mBinding.protocolPrompt)
        initViewModelObserve()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        LoginUtils.synchEnvironmentLogo(mBinding.logo)
    }


    private fun initData() {

    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mIsLogoContinuousClick.observe(this, Observer {
            mBinding.changeEnvironment.setVisible(it)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.login,
            mBinding.changeEnvironment,
            mBinding.logo,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.login -> {
                        when {
                            mBinding.protocolCheckbox.isChecked ->{
                                InputPhoneAtivity.actionStart()
                            }
                            else ->
                                LoginUtils.delayedProtocolPrompt(mBinding.protocolPrompt)
                        }

                    }
                    mBinding.changeEnvironment -> EnvironmentSwitchActivity.actionStart()
                    mBinding.logo -> mViewModel.continuousClick()
                }
            }
        )
    }
}


