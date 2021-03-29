package com.tanlifei.app.main.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.customspan.CustomClickableSpan
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.ComUtils
import com.common.widget.component.extension.*
import com.common.widget.TextInputHelper
import com.tanlifei.app.R
import com.tanlifei.app.common.config.api.ApiUrlConst.URL_PRIVATE_AGREEMENT
import com.tanlifei.app.common.config.api.ApiUrlConst.URL_USER_AGREEMENT
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivityLoginBinding
import com.tanlifei.app.main.utils.LoginUtils
import com.tanlifei.app.main.viewmodel.LoginViewModel


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), TextWatcher {

    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart() {
            startActivity<LoginAtivity> { }
            ActivityUtils.finishOtherActivities(LoginAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }


    override fun init() {
        setProtocolTxt()
        initTextInputHelper()
        initViewModelObserve()
        initListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        mBinding.logo.setImageResource(EnvironmentUtils.appLogo())
    }


    private fun initData() {

    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mUiChange.observe(this, this)

        mViewModel.mIsToken.observe(this, Observer {
            if (it) {
                ComFun.mToken = mViewModel.mToken
                mViewModel.mToken?.let { it -> UserInfoUtils.saveToken(it) }
                MainActivity.actionStart()
            }
        })

        mViewModel.mSmsCodeInterval.observe(this, Observer {
            when (it) {
                -1L -> onIntervalStart()
                -2L -> onIntervalComplete()
                else -> onIntervalChanged(it)
            }
        })

        mViewModel.mIsContinuousClick.observe(this, Observer {
            mBinding.changeEnvironment.setVisible(it)
        })

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        mBinding.phone.addTextChangedListener(this)
        clickListener(
            mBinding.codeBtn,
            mBinding.login,
            mBinding.changeEnvironment,
            mBinding.logo,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.codeBtn -> {
                        if (checkPhone(
                                LoginUtils.getPhone(mBinding.phone.text.toString())
                            )
                        ) {
                            mViewModel.requestSmsCode(LoginUtils.getPhone(mBinding.phone.text.toString()))
                        }
                    }
                    mBinding.login -> {
                        if (checkFormInfo(
                                LoginUtils.getPhone(mBinding.phone.text.toString()),
                                mBinding.code.text.toString()
                            )
                        ) {
                            mViewModel.requestLogin(
                                LoginUtils.getPhone(mBinding.phone.text.toString()),
                                mBinding.code.text.toString()
                            )
                        }
                    }
                    mBinding.changeEnvironment -> EnvironmentSwitchActivity.actionStart()
                    mBinding.logo -> mViewModel.continuousClick()
                }
            }
        )
    }


    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this, mBinding.login)
        mInputHelper.addViews(mBinding.phone, mBinding.code)
    }


    /**
     * 登录即代表您已同意《用户协议》和《隐私政策》
     */
    private fun setProtocolTxt() {
        val protocolBuild = SimplifySpanBuild()
        protocolBuild.append(SpecialTextUnit("登录即代表您已同意").setTextColor(color(R.color.txt_sub)))
        protocolBuild.append(SpecialTextUnit("《用户协议》").setTextColor(color(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(mBinding.protocolTxt,
                    OnClickableSpanListener { v: TextView, _: CustomClickableSpan? ->
                        if (v.clickEnable()) {
                            gotoWeb(
                                "用户协议",
                                URL_USER_AGREEMENT
                            )
                        }
                    }
                )
                    .setPressBgColor(color(R.color.white))
            ))
        protocolBuild.append(SpecialTextUnit("和").setTextColor(color(R.color.txt_sub)))
        protocolBuild.append(SpecialTextUnit("《隐私政策》").setTextColor(color(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(mBinding.protocolTxt,
                    OnClickableSpanListener { v: TextView, _: CustomClickableSpan? ->
                        if (v.clickEnable()) {
                            gotoWeb(
                                "隐私政策",
                                URL_PRIVATE_AGREEMENT
                            )
                        }
                    }
                )
                    .setPressBgColor(color(R.color.white))))
        mBinding.protocolTxt.text = protocolBuild.build()
    }


    /**
     * 校验表单信息
     */
    private fun checkFormInfo(phone: String, code: String): Boolean {
        if (phone.isEmpty()) {
            toast("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            toast("请输入正确的手机号码")
            return false
        }
        if (code.isEmpty()) {
            toast("请输入验证码")
            return false
        } else if (code.length < 4) {
            toast("请输入4位验证码")
            return false
        }
        return true
    }

    /**
     * 校验手机号
     */
    private fun checkPhone(phone: String): Boolean {
        if (phone.isEmpty()) {
            toast("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            toast("请输入正确的手机号码")
            return false
        }
        return true
    }

    /**
     * webView查看协议
     */
    private fun gotoWeb(title: String, url: String) {
        BaseWebViewActivity.actionStart(title, url)
    }

    /**
     * 设置触摸不收起键盘控件
     */
    override fun showSoftByEditView(): MutableList<View> {
        return mutableListOf(mBinding.phone, mBinding.code)
    }


    /**
     * 倒计时开始时显示
     */
    private fun onIntervalStart() {
        mBinding.codeBtn.isClickable = false //在发送数据的时候设置为不能点击
        mBinding.codeBtn.setTextColor(color(R.color.txt_help))
    }

    /**
     * 倒计时正行中显示
     */

    private fun onIntervalChanged(second: Long) {
        mBinding.codeBtn.text = "${second}s"
    }

    /**
     * 倒计时结束后显示
     */
    private fun onIntervalComplete() {
        mBinding.codeBtn.isClickable = true
        mBinding.codeBtn.setTextColor(color(R.color.theme))
        mBinding.codeBtn.text = "点击获取"
    }

    override fun onBackPressed() {
        ComUtils.exitApp()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mInputHelper?.removeViews()
    }

    override fun afterTextChanged(s: Editable?) {
        mBinding.phone.setSelection(mBinding.phone.text.toString().length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /**
     * 格式化手机号格式（三四四格式） 如：138 2132 3435
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        LoginUtils.phoneFormatTextChanged(mBinding.phone, s, count)
    }


}


