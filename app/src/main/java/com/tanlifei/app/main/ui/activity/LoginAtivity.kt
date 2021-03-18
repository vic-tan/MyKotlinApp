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
import com.common.core.base.ui.activity.BaseFormActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.EnvironmentSwitchActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.ComUtils
import com.common.utils.extension.clickEnable
import com.common.utils.extension.clickListener
import com.common.utils.extension.color
import com.common.widget.TextInputHelper
import com.hjq.toast.ToastUtils
import com.tanlifei.app.R
import com.tanlifei.app.common.config.api.ApiConst.URL_PRIVATE_AGREEMENT
import com.tanlifei.app.common.config.api.ApiConst.URL_USER_AGREEMENT
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivityLoginBinding
import com.tanlifei.app.main.utils.LoginUtils
import com.tanlifei.app.main.viewmodel.LoginViewModel


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseFormActivity<ActivityLoginBinding, LoginViewModel>(), TextWatcher {

    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(LoginAtivity::class.java)
            ActivityUtils.finishOtherActivities(LoginAtivity::class.java)
        }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel()
    }

    override fun hideToolbar(): Boolean {
        return true
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
        binding.logo.setImageResource(EnvironmentUtils.appLogo())
    }


    private fun initData() {

    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.loadingState.observe(this, this)

        viewModel.isToken.observe(this, Observer {
            if (it) {
                ComFun.token = viewModel.token
                viewModel.token?.let { it -> UserInfoUtils.saveToken(it) }
                MainActivity.actionStart()
            }
        })

        viewModel.smsCodeInterval.observe(this, Observer {
            when (it) {
                -1L -> onIntervalStart()
                -2L -> onIntervalComplete()
                else -> onIntervalChanged(it)
            }
        })

        viewModel.isContinuousClick.observe(this, Observer {
            binding.changeEnvironment.visibility = if (it) View.VISIBLE else View.GONE
        })

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        binding.phone.addTextChangedListener(this)
        clickListener(
            binding.codeBtn,
            binding.login,
            binding.changeEnvironment,
            binding.logo,
            clickListener = View.OnClickListener {
                when (it) {
                    binding.codeBtn -> {
                        if (checkPhone(
                                LoginUtils.getPhone(binding.phone.text.toString())
                            )
                        ) {
                            viewModel.requestSmsCode(LoginUtils.getPhone(binding.phone.text.toString()))
                        }
                    }
                    binding.login -> {
                        if (checkFormInfo(
                                LoginUtils.getPhone(binding.phone.text.toString()),
                                binding.code.text.toString()
                            )
                        ) {
                            viewModel.requestLogin(
                                LoginUtils.getPhone(binding.phone.text.toString()),
                                binding.code.text.toString()
                            )
                        }
                    }
                    binding.changeEnvironment -> EnvironmentSwitchActivity.actionStart()
                    binding.logo -> viewModel.continuousClick()
                }
            }
        )
    }


    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(this,binding.login)
        mInputHelper.addViews(binding.phone, binding.code)
    }


    /**
     * 登录即代表您已同意《用户协议》和《隐私政策》
     */
    private fun setProtocolTxt() {
        val protocolBuild = SimplifySpanBuild()
        protocolBuild.append(SpecialTextUnit("登录即代表您已同意").setTextColor(color(R.color.color_666666)))
        protocolBuild.append(SpecialTextUnit("《用户协议》").setTextColor(color(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(binding.protocolTxt,
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
        protocolBuild.append(SpecialTextUnit("和").setTextColor(color(R.color.color_666666)))
        protocolBuild.append(SpecialTextUnit("《隐私政策》").setTextColor(color(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(binding.protocolTxt,
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
        binding.protocolTxt.text = protocolBuild.build()
    }


    /**
     * 校验表单信息
     */
    private fun checkFormInfo(phone: String, code: String): Boolean {
        if (phone.isEmpty()) {
            ToastUtils.show("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.show("请输入正确的手机号码")
            return false
        }
        if (code.isEmpty()) {
            ToastUtils.show("请输入验证码")
            return false
        } else if (code.length < 4) {
            ToastUtils.show("请输入4位验证码")
            return false
        }
        return true
    }

    /**
     * 校验手机号
     */
    private fun checkPhone(phone: String): Boolean {
        if (phone.isEmpty()) {
            ToastUtils.show("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.show("请输入正确的手机号码")
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
    override fun showSoftByEditViewIds(): IntArray {
        return intArrayOf(R.id.phone, R.id.code)
    }


    /**
     * 倒计时开始时显示
     */
    private fun onIntervalStart() {
        binding.codeBtn.isClickable = false //在发送数据的时候设置为不能点击
        binding.codeBtn.setTextColor(color(R.color.color_999999))
    }

    /**
     * 倒计时正行中显示
     */

    private fun onIntervalChanged(second: Long) {
        binding.codeBtn.text = "${second}s"
    }

    /**
     * 倒计时结束后显示
     */
    private fun onIntervalComplete() {
        binding.codeBtn.isClickable = true
        binding.codeBtn.setTextColor(color(R.color.theme_color))
        binding.codeBtn.text = "点击获取"
    }

    override fun onBackPressed() {
        ComUtils.exitApp()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mInputHelper?.removeViews()
    }

    override fun afterTextChanged(s: Editable?) {
        binding.phone.setSelection(binding.phone.text.toString().length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /**
     * 格式化手机号格式（三四四格式） 如：138 2132 3435
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        LoginUtils.phoneFormatTextChanged(binding.phone, s, count)
    }

}


