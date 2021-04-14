package com.onlineaginguniversity.login.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.ui.activity.BaseActivity
import com.common.constant.GlobalEnumConst
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.core.share.listener.OnAuthListener
import com.common.core.share.utils.AuthUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.setVisible
import com.common.widget.component.extension.startActivity
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivityLoginEntranceBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel
import java.util.*


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
        uiChangeObserve()
        mViewModel.mIsLogoContinuousClick.observe(this, Observer {
            mBinding.changeEnvironment.setVisible(it)
        })
        mViewModel.mWxLoginResult.observe(this, Observer {
            LoginUtils.wxBind(it)
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
            mBinding.wxLogin,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.login -> {
                        when {
                            mBinding.protocolCheckbox.isChecked -> {
                                InputPhoneAtivity.actionStart(EnumConst.SMSType.MOBILE_LOGIN)
                            }
                            else ->
                                LoginUtils.delayedProtocolPrompt(mBinding.protocolPrompt)
                        }

                    }
                    mBinding.changeEnvironment -> EnvironmentSwitchActivity.actionStart()
                    mBinding.logo -> mViewModel.continuousClick()
                    mBinding.wxLogin -> {
                        AuthUtils.wechatAuth(object : OnAuthListener {
                            override fun onComplete(
                                type: GlobalEnumConst.ShareType,
                                prams: HashMap<String, Any>?
                            ) {
                                mViewModel.requestWechatLogin(prams?.get("openid") as String)
                            }
                        })
                    }
                }
            }
        )
    }
}


