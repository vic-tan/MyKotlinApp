package com.tanlifei.app.main.ui

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.customspan.CustomClickableSpan
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.ComApplication
import com.common.base.ui.activity.BaseFormActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.environment.EnvironmentSwitchActivity
import com.common.utils.ResUtils
import com.common.widget.TextInputHelper
import com.hjq.toast.ToastUtils
import com.tanlifei.app.R
import com.tanlifei.app.common.config.UrlConst.URL_AGREEMENT
import com.tanlifei.app.common.config.UrlConst.URL_REGISTER_AGREEMENT
import com.tanlifei.app.common.utils.AppUtils
import com.tanlifei.app.databinding.ActivityLoginBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.model.LoginViewModel
import com.tanlifei.app.main.model.factory.LoginModelFactory
import com.tanlifei.app.main.network.LoginNetwork
import com.tanlifei.app.main.utils.LoginUtils


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseFormActivity<ActivityLoginBinding>(),
    LoginViewModel.OnIntervalListener, TextWatcher {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mInputHelper: TextInputHelper

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(LoginAtivity::class.java)
        }
    }

    override fun layoutResId(): Int {
        return R.layout.activity_login
    }

    override fun createBinding(layoutView: View): ActivityLoginBinding {
        return ActivityLoginBinding.bind(layoutView)
    }


    override fun initView() {
        setProtocolTxt()
        loginViewModel = ViewModelProvider(
            this,
            LoginModelFactory(LoginNetwork.getInstance())
        ).get(
            LoginViewModel::class.java
        )
        loginViewModel.setOnIntervalListener(this)
        binding.codeBtn.setOnClickListener {
            if (checkPhone(
                    LoginUtils.getPhone(binding.phone.text.toString())
                )
            ) {
                loginViewModel.getCode(LoginUtils.getPhone(binding.phone.text.toString()))
            }
        }
        loginViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) hud.show()
            else hud.dismiss()
        })

        loginViewModel.isToken.observe(this, Observer {
            if (it) {
                ComApplication.token = loginViewModel.token
                HomeActivity.actionStart()
            }
        })
        initTextInputHelper()
        binding.phone.addTextChangedListener(this)
        binding.login.setOnClickListener {
            if (checkFormInfo(
                    LoginUtils.getPhone(binding.phone.text.toString()),
                    binding.code.toString()
                )
            ) {
                loginViewModel.login(
                    LoginUtils.getPhone(binding.phone.text.toString()),
                    binding.code.toString()
                )
            }
        }

        loginViewModel.initEnvironmentSwitcher()//初始化环境切换器
        binding.changeEnvironment.setOnClickListener {
            EnvironmentSwitchActivity.actionStart(
                this,
                loginViewModel.environmentList
            )
        }
    }

    /**
     * 初始化输入框内容是否禁用按钮监听
     */
    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(binding.login)
        mInputHelper.addViews(binding.phone, binding.code)
    }

    /**
     * 登录即代表您已同意《用户协议》和《隐私政策》
     */
    private fun setProtocolTxt() {
        val protocolBuild = SimplifySpanBuild()
        protocolBuild.append(SpecialTextUnit("登录即代表您已同意").setTextColor(ResUtils.getColor(R.color.color_666666)))
        protocolBuild.append(SpecialTextUnit("《用户协议》").setTextColor(ResUtils.getColor(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(binding.protocolTxt,
                    OnClickableSpanListener { _: TextView?, _: CustomClickableSpan? ->
                        gotoWeb(
                            URL_REGISTER_AGREEMENT,
                            "用户协议"
                        )
                    }
                )
                    .setPressBgColor(ResUtils.getColor(R.color.white))
            ))
        protocolBuild.append(SpecialTextUnit("和").setTextColor(ResUtils.getColor(R.color.color_666666)))
        protocolBuild.append(SpecialTextUnit("《隐私政策》").setTextColor(ResUtils.getColor(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(binding.protocolTxt,
                    OnClickableSpanListener { _: TextView?, _: CustomClickableSpan? ->
                        gotoWeb(
                            URL_AGREEMENT,
                            "隐私政策"
                        )
                    }
                )
                    .setPressBgColor(ResUtils.getColor(R.color.white))))
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
    private fun gotoWeb(url: String, title: String) {
        BaseWebViewActivity.actionStart(this, url, title)
    }

    override fun showSoftByEditViewIds(): IntArray {
        return intArrayOf(R.id.phone, R.id.code)
    }


    /**
     * 倒计时开始时显示
     */
    override fun onIntervalStart() {
        binding.codeBtn.isClickable = false //在发送数据的时候设置为不能点击
        binding.codeBtn.setTextColor(ResUtils.getColor(R.color.color_999999))
    }

    /**
     * 倒计时正行中显示
     */
    @SuppressLint("SetTextI18n")
    override fun onIntervalChanged(second: Long) {
        binding.codeBtn.text = "${second}s"
    }

    /**
     * 倒计时结束后显示
     */
    override fun onIntervalComplete() {
        binding.codeBtn.isClickable = true
        binding.codeBtn.setTextColor(ResUtils.getColor(R.color.theme_color))
        binding.codeBtn.text = "点击获取"
    }

    override fun onBackPressed() {
        AppUtils.exitApp()
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

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        LoginUtils.phoneFormatTextChanged(binding.phone, s, count)
    }
}


