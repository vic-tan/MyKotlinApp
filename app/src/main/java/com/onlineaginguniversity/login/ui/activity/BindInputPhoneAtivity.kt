package com.onlineaginguniversity.login.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.core.http.RxHttpManager
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivityBindPhoneBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:绑定输入手机界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class BindInputPhoneAtivity :
    BaseToolBarActivity<ActivityBindPhoneBinding, LoginViewModel>() {

    private lateinit var mInputHelper: TextInputHelper
    private var openId: String = ""

    companion object {
        fun actionStart(openId: String) {
            startActivity<BindInputPhoneAtivity> {
                putExtra(GlobalConst.Extras.ID, openId)
            }
            ActivityUtils.finishOtherActivities(BindInputPhoneAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        mTitleBar.titleView.gone()
        mTitleBar.lineView.gone()
        mTitleBar.leftView.visibility = View.INVISIBLE
        openId = intent.getStringExtra(GlobalConst.Extras.ID).toString()
        initViewModelObserve()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        LoginUtils.synchEnvironmentLogo(mBinding.logo)
    }


    private fun initData() {
        initTextInputHelper()
        LoginUtils.phoneFormat(mBinding.phone)
    }

    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this, mBinding.enter)
        mInputHelper.addViews(mBinding.code, mBinding.phone)
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

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.enter,
            mBinding.codeBtn,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.enter -> {
                        login()
                    }
                    mBinding.codeBtn -> {
                        mViewModel.requestSmsCode(
                            LoginUtils.getPhoneNumber(mBinding.phone.text.toString()),
                            EnumConst.SMSType.MOBILE_LOGIN
                        )
                    }
                }
            }
        )
    }

    fun login() {
        if (!LoginUtils.checkPhone(mBinding.phone)) {
            return
        }
        if (ObjectUtils.isEmpty(mBinding.code.text.toString())) {
            toast("请输入验证码")
            return
        }
        mViewModel.requestCodeLogin(
            LoginUtils.getPhoneNumber(mBinding.phone.text.toString()),
            mBinding.code.text.toString()
        )
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.code, mBinding.phone)
    }

}


