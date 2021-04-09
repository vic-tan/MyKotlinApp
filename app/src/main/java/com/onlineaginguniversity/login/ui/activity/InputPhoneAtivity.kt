package com.onlineaginguniversity.login.ui.activity

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.onlineaginguniversity.databinding.ActivityInputPhoneBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.login.viewmodel.LoginViewModel


/**
 * @desc:填写手机号界面
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
class InputPhoneAtivity :
    BaseToolBarActivity<ActivityInputPhoneBinding, LoginViewModel>() {

    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart() {
            startActivity<InputPhoneAtivity> { }
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
        mInputHelper = TextInputHelper(this, mBinding.code)
        mInputHelper.addViews(mBinding.phone)
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.code,
            mBinding.pwdLogin,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.code -> {
                        if (LoginUtils.checkPhone(mBinding.phone)) {
                            PhoneLoginAtivity.actionStart(mBinding.phone.text.toString())
                            ActivityUtils.finishActivity(mActivity)
                        }
                    }
                    mBinding.pwdLogin -> EnvironmentSwitchActivity.actionStart()
                }
            }
        )
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.phone)
    }

}


