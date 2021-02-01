package com.tanlifei.app.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.common.ComApplication
import com.common.base.ui.activity.BaseFormActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.utils.ResUtils
import com.common.widget.TextInputHelper
import com.tanlifei.app.BaseApplication
import com.tanlifei.app.R
import com.tanlifei.app.common.config.UrlConst.AGREEMENT
import com.tanlifei.app.common.config.UrlConst.REGISTER_AGREEMENT
import com.tanlifei.app.common.utils.AppUtils
import com.tanlifei.app.databinding.ActivityLoginBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.model.factory.LoginModelFactory
import com.tanlifei.app.main.model.LoginViewModel
import com.tanlifei.app.main.network.LoginNetwork
import com.tanlifei.app.main.utils.LoginUtils
import com.xiaomai.environmentswitcher.EnvironmentSwitchActivity

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseFormActivity<ActivityLoginBinding>(),
    LoginViewModel.OnIntervalListener, TextWatcher {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mInputHelper: TextInputHelper

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

            if (loginViewModel.checkPhone(
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
                ActivityUtils.startActivity(HomeActivity::class.java)
            }
        })
        initTextInputHelper()
        binding.phone.addTextChangedListener(this)
        binding.login.setOnClickListener {
            if (loginViewModel.checkFormInfo(
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
        binding.changeEnvironment.setOnClickListener { EnvironmentSwitchActivity.launch(this) }
    }


    private fun initTextInputHelper() {
        mInputHelper = TextInputHelper(binding.login)
        mInputHelper.addViews(binding.phone, binding.code)
    }

    private fun setProtocolTxt() {
        //登录即代表您已同意《用户协议》和《隐私政策》
        val protocolBuild = SimplifySpanBuild()
        protocolBuild.append(SpecialTextUnit("登录即代表您已同意").setTextColor(ResUtils.getColor(R.color.color_666666)))
        protocolBuild.append(SpecialTextUnit("《用户协议》").setTextColor(ResUtils.getColor(R.color.color_A47E68))
            .setClickableUnit(
                SpecialClickableUnit(binding.protocolTxt,
                    OnClickableSpanListener { _: TextView?, _: CustomClickableSpan? ->
                        gotoWeb(
                            REGISTER_AGREEMENT,
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
                            AGREEMENT,
                            "隐私政策"
                        )
                    }
                )
                    .setPressBgColor(ResUtils.getColor(R.color.white))))
        binding.protocolTxt.text = protocolBuild.build()
    }

    private fun gotoWeb(url: String, title: String) {
        var intent = Intent(this, BaseWebViewActivity::class.java)
        var bundle = Bundle()
        bundle.putString(BaseWebViewActivity.EXTRAS_URL, url)
        bundle.putString(BaseWebViewActivity.EXTRAS_TITLE, title)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun showSoftByEditViewIds(): IntArray {
        return intArrayOf(R.id.phone, R.id.code)
    }


    override fun onIntervalStart() {
        binding.codeBtn.isClickable = false //在发送数据的时候设置为不能点击
        binding.codeBtn.setTextColor(ResUtils.getColor(R.color.color_999999))
    }

    @SuppressLint("SetTextI18n")
    override fun onIntervalChanged(second: Long) {
        binding.codeBtn.text = "${second}s"
    }

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


