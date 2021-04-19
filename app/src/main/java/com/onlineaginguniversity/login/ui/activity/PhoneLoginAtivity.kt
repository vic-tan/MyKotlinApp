package com.onlineaginguniversity.login.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.utils.ComDialogUtils
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivityPhoneLoginBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:手机号登录界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class PhoneLoginAtivity :
    BaseToolBarActivity<ActivityPhoneLoginBinding, LoginViewModel>() {

    private var phoneNumber: String = ""
    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart(phoneNumber: String) {
            startActivity<PhoneLoginAtivity> {
                putExtra(GlobalConst.Extras.NAME, phoneNumber)
            }
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        mTitleBar.lineView.gone()
        mTitleBar.titleView.gone()
        phoneNumber = intent.getStringExtra(GlobalConst.Extras.NAME).toString()
        initViewModelObserve()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        LoginUtils.synchEnvironmentLogo(mBinding.logo)
    }


    private fun initData() {
        mBinding.prompt.text = "短信验证码已发送至 $phoneNumber"
        mViewModel.requestSmsCode(LoginUtils.getPhoneNumber(phoneNumber),EnumConst.SMSType.MOBILE_LOGIN)
        initTextInputHelper()
    }

    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this, mBinding.login)
        mInputHelper.addViews(mBinding.code)
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mCodeTimer.observe(this, Observer {
            when (it) {
                EnumConst.TimerConst.START.value -> LoginUtils.onTimerStart(mBinding.codeBtn)
                EnumConst.TimerConst.COMPLETE.value -> LoginUtils.onTimerComplete(mBinding.codeBtn)
                else -> LoginUtils.onTimerChanged(mBinding.codeBtn, it)
            }
        })
        mViewModel.mIsToken.observe(this, Observer {
            mViewModel.mToken?.let { it -> LoginUtils.loginSuccess(it) }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.login,
            mBinding.codeBtn,
            mBinding.notReceivedCode,
            mBinding.voiceCode,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.login -> {
                        if (LoginUtils.checkCode(mBinding.code)) {
                            mViewModel.requestCodeLogin(
                                LoginUtils.getPhoneNumber(phoneNumber),
                                mBinding.code.text.toString()
                            )
                        }
                    }
                    mBinding.codeBtn -> {
                        mViewModel.requestSmsCode(
                            LoginUtils.getPhoneNumber(phoneNumber),
                            EnumConst.SMSType.MOBILE_LOGIN
                        )
                    }
                    mBinding.notReceivedCode -> {
                        ComDialogUtils.showComPrompt(
                            this@PhoneLoginAtivity,
                            "收不到验证码?",
                            "1.手机号码是否输入正确\n2.检查手机是否停机\n3.请使用其他账号登录"
                        )
                    }
                    mBinding.voiceCode -> {
                        mViewModel.requestVoiceCode(
                            LoginUtils.getPhoneNumber(phoneNumber),
                            EnumConst.SMSType.MOBILE_LOGIN
                        )
                    }
                }
            }
        )
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.code)
    }

}


