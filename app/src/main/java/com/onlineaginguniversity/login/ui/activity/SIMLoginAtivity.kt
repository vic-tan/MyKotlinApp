package com.onlineaginguniversity.login.ui.activity

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseActivity
import com.common.widget.component.extension.startActivity
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.onlineaginguniversity.databinding.ActivitySimLoginBinding
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.ui.widget.CustomOneKeyLoginView
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.utils.OnKeyLoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:阿里一键登录界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class SIMLoginAtivity :
    BaseActivity<ActivitySimLoginBinding, LoginViewModel>() {

    private var authHelper: PhoneNumberAuthHelper? = null
    private val mAuthHelper: PhoneNumberAuthHelper get() = authHelper!!

    private lateinit var oneKeyView: CustomOneKeyLoginView
    private var tokenResult: OnKeyLoginListener.TokenResult? = null

    companion object {
        fun actionStart() {
            startActivity<SIMLoginAtivity> {}
            ActivityUtils.finishOtherActivities(SIMLoginAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        initOneKey()
        initViewModelObserve()
        initListener()
        initData()
    }


    private fun initData() {

    }

    private fun initOneKey() {
        tokenResult = object : OnKeyLoginListener.TokenResult {
            override fun success(token: String) {
                mViewModel.requestOneKeyLogin(token)
                authHelper = null
            }

            override fun failure() {
                authHelper = null
            }

            override fun backPressed() {
                authHelper = null
                ActivityUtils.finishAllActivities()
                ActivityUtils.finishActivity(this@SIMLoginAtivity)
            }

        }
        authHelper = OnKeyLoginUtils.getAuthHelper(
            tokenResult as OnKeyLoginListener.TokenResult
        )
        oneKeyView =
            CustomOneKeyLoginView(mAuthHelper, object : OnKeyLoginListener.UIClickListener {})
        oneKeyView.configAuthPage()
        mAuthHelper.getLoginToken(this, 5000)
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mIsToken.observe(this, Observer {
            mViewModel.mToken?.let { it -> LoginUtils.loginSuccess(it) }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {

    }

    override fun onBackPressed() {
        ActivityUtils.finishAllActivities()
        ActivityUtils.finishActivity(this@SIMLoginAtivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        tokenResult = null
    }

}


