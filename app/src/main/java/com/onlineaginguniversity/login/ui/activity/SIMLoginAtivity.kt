package com.onlineaginguniversity.login.ui.activity

import android.os.Environment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseActivity
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.core.environment.event.EnvironmentEvent
import com.common.core.event.BaseEvent
import com.common.widget.component.extension.startActivity
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.onlineaginguniversity.common.event.UserEvent
import com.onlineaginguniversity.databinding.ActivitySimLoginBinding
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.ui.widget.CustomOneKeyLoginView
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.utils.OnKeyLoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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


    companion object {
        fun actionStart() {
            startActivity<SIMLoginAtivity> {}
            ActivityUtils.finishOtherActivities(SIMLoginAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun registerEventBus(): Boolean {
        return true
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
        authHelper = OnKeyLoginUtils.getAuthHelper(object : OnKeyLoginListener.TokenResult {
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

        })
        oneKeyView =
            CustomOneKeyLoginView(mAuthHelper, object : OnKeyLoginListener.UIClickListener {
                override fun clickEnvironment() {
                    EnvironmentSwitchActivity.actionStart()
                }
            })
        oneKeyView.configAuthPage()
        mAuthHelper.getLoginToken(ComFun.mContext, 5000)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: BaseEvent) {
        if (event is EnvironmentEvent) {
            oneKeyView?.let {
                it.updateLogo()
            }
        }
    }

    override fun onBackPressed() {
        ActivityUtils.finishAllActivities()
        ActivityUtils.finishActivity(this@SIMLoginAtivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        authHelper = null
    }

}


