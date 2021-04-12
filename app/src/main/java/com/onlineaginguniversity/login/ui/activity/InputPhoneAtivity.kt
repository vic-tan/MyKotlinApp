package com.onlineaginguniversity.login.ui.activity

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.constant.GlobalConst
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.widget.TextInputHelper
import com.common.widget.component.extension.*
import com.onlineaginguniversity.common.constant.EnumConst
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
    private lateinit var type: EnumConst.SMSType

    companion object {
        fun actionStart(type: EnumConst.SMSType) {
            startActivity<InputPhoneAtivity> {
                putExtra(GlobalConst.Extras.TYPE, type)
            }
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        type = intent.getSerializableExtra(GlobalConst.Extras.TYPE) as EnumConst.SMSType
        mTitleBar.lineView.gone()
        mBinding.pwdLogin.setVisible(type == EnumConst.SMSType.MOBILE_LOGIN)
        when (type) {
            //登录
            EnumConst.SMSType.MOBILE_LOGIN -> {
                mBinding.title.text = "手机号登录"
                mBinding.hint.text = "首页登录将自动注册"
            }
            //找回密码
            EnumConst.SMSType.RETRIEVE_PASSWORD -> {
                mBinding.title.text = "手机验证"
                mBinding.hint.text = "验证手机号码后，设置新密码"
            }
        }
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
                        startCode()
                    }
                    mBinding.pwdLogin -> PwdLoginAtivity.actionStart()
                }
            }
        )
    }

    fun startCode() {
        if (!LoginUtils.checkPhone(mBinding.phone)) {
            return
        }
        when (type) {
            //登录
            EnumConst.SMSType.MOBILE_LOGIN -> {
                PhoneLoginAtivity.actionStart(mBinding.phone.text.toString())
                ActivityUtils.finishActivity(mActivity)
            }
            //找回密码
            EnumConst.SMSType.RETRIEVE_PASSWORD -> {
                SetPasswordAtivity.actionStart(mBinding.phone.text.toString(), type)
            }
        }
        ActivityUtils.finishActivity(this@InputPhoneAtivity)
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.phone)
    }

}


