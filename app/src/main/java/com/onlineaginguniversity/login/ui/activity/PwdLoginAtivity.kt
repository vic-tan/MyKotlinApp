package com.onlineaginguniversity.login.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.utils.ComDialogUtils
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.*
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.databinding.ActivityPwdLoginBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:密码登录界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class PwdLoginAtivity :
    BaseToolBarActivity<ActivityPwdLoginBinding, LoginViewModel>() {

    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart() {
            startActivity<PwdLoginAtivity> {}
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        mTitleBar.lineView.gone()
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
        mInputHelper = TextInputHelper(this, mBinding.login)
        mInputHelper.addViews(mBinding.phone, mBinding.pwd)
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mIsToken.observe(this, Observer {
            mViewModel.mToken?.let { it -> LoginUtils.loginSuccess(it) }
        })
        mViewModel.mPwdLoginResult.observe(this, Observer {
            if (ObjectUtils.isNotEmpty(it)) {
                if (it.status == 0) {
                    it.token?.let { token -> LoginUtils.loginSuccess(token) }
                } else if (it.status == -1) {
                    when (it.count) {
                        1 -> {
                            mBinding.errorText.text = "账号或密码错误"
                            mBinding.errorText.visible()
                        }
                        in 2..5 -> {
                            mBinding.errorText.text =
                                "账号或密码错误，还有${6 - it.count}次机会"
                            mBinding.errorText.visible()
                        }
                        -1 -> {
                            mBinding.errorText.gone()
                            var content = "账号或密码错误已超过5次， \n请在${it.expireTime}分钟后重试？"
                            ComDialogUtils.showMultDialogByNoTitle(this@PwdLoginAtivity,
                                content,
                                "忘记密码",
                                OnConfirmListener {
                                    SetPasswordAtivity.actionStart(
                                        mBinding.phone.text.toString(),
                                        EnumConst.SMSType.RETRIEVE_PASSWORD
                                    )
                                })
                        }
                    }
                }
            } else {
                toast("登录失败")
            }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.login,
            mBinding.forgetPwd,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.login -> {
                        login()
                    }
                    mBinding.forgetPwd -> {
                        InputPhoneAtivity.actionStart(EnumConst.SMSType.RETRIEVE_PASSWORD)
                    }
                }
            }
        )
    }

    fun login() {
        if (ObjectUtils.isEmpty(mBinding.phone.text.toString())) {
            toast("请输入手机号")
            return
        }
        if (!RegexUtils.isMobileExact(LoginUtils.getPhoneNumber(mBinding.phone.text.toString()))) {
            toast("请输入正确的手机号码")
            return
        }
        if (ObjectUtils.isEmpty(mBinding.pwd.text.toString())) {
            toast("请输入密码")
            return
        }
        mViewModel.requestPwdLogin(
            LoginUtils.getPhoneNumber(mBinding.phone.text.toString()),
            mBinding.pwd.text.toString()
        )
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.phone, mBinding.pwd)
    }

}


