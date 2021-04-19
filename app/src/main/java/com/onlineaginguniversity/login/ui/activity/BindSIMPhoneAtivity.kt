package com.onlineaginguniversity.login.ui.activity

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseActivity
import com.common.constant.GlobalConst
import com.common.core.http.RxHttpManager
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivitySimLoginBinding
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.ui.widget.OneKeyView
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.utils.OnKeyLoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:一键绑定SIM手机号界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class BindSIMPhoneAtivity :
    BaseActivity<ActivitySimLoginBinding, LoginViewModel>() {

    private var authHelper: PhoneNumberAuthHelper? = null
    private val mAuthHelper: PhoneNumberAuthHelper get() = authHelper!!

    private lateinit var oneKeyView: OneKeyView
    private var openId: String = ""

    companion object {
        fun actionStart(openId: String) {
            startActivity<BindSIMPhoneAtivity> {
                putExtra(GlobalConst.Extras.ID, openId)
            }
            ActivityUtils.finishOtherActivities(BindSIMPhoneAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }

    override fun init() {
        mTitleBar.titleView.gone()
        openId = intent.getStringExtra(GlobalConst.Extras.ID).toString()
        initOneKey()
        initViewModelObserve()
    }


    private fun initOneKey() {
        authHelper = OnKeyLoginUtils.getAuthHelper(object : OnKeyLoginListener.TokenResult {
            override fun authPageSuccess(token: String) {
                mViewModel.requestOneKeyLogin(token)
                authHelper = null
            }

            override fun authPageFail() {
                BindInputPhoneAtivity.actionStart(openId)
                authHelper = null
            }

            override fun fail() {
                authHelper = null
            }

            override fun backPressed() {
                authHelper = null
                ActivityUtils.finishAllActivities()
                ActivityUtils.finishActivity(this@BindSIMPhoneAtivity)
            }

        })
        oneKeyView =
            OneKeyView(mAuthHelper, object : OnKeyLoginListener.UIClickListener {
                override fun clickOtherBtn() {
                    BindInputPhoneAtivity.actionStart(openId)
                }
            }, isLogin = false)
        oneKeyView.configAuthPage()
        mAuthHelper.getLoginToken(ComFun.mContext, 5000)
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mWxBindPhone.observe(this, Observer {
            mViewModel.mToken?.let { token ->
                LoginUtils.loginSuccess(token)
            }
        })

        mViewModel.mIsToken.observe(this, Observer {
            mViewModel.mToken?.let {
                ComFun.mToken = it
                RxHttpManager.addToken()
                mViewModel.requestBindPhoneLogin(openId)
            }
        })
    }


    override fun onBackPressed() {
        ActivityUtils.finishAllActivities()
        ActivityUtils.finishActivity(this@BindSIMPhoneAtivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        authHelper = null
    }

}


