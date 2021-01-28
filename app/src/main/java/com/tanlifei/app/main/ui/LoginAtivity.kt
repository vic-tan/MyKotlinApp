package com.tanlifei.app.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import cn.iwgang.simplifyspan.SimplifySpanBuild
import cn.iwgang.simplifyspan.customspan.CustomClickableSpan
import cn.iwgang.simplifyspan.other.OnClickableSpanListener
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit
import cn.iwgang.simplifyspan.unit.SpecialTextUnit
import com.blankj.utilcode.util.ActivityUtils
import com.common.base.ui.activity.BaseFormActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.utils.ResUtils
import com.tanlifei.app.R
import com.tanlifei.app.common.utils.AppUtils
import com.tanlifei.app.databinding.ActivityLoginBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.model.LoginViewModel
import com.tanlifei.app.main.model.LoginViewModel.Companion.AGREEMENT
import com.tanlifei.app.main.model.LoginViewModel.Companion.REGISTER_AGREEMENT

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseFormActivity<ActivityLoginBinding>(),
    LoginViewModel.OnIntervalListener {

    private lateinit var loginViewModel: LoginViewModel

    override fun layoutResId(): Int {
        return R.layout.activity_login
    }

    override fun createBinding(layoutView: View): ActivityLoginBinding {
        return ActivityLoginBinding.bind(layoutView)
    }


    override fun initView() {
        setProtocolTxt()
        binding.login.setOnClickListener { ActivityUtils.startActivity(HomeActivity::class.java) }
        loginViewModel = ViewModelProvider(this, NewInstanceFactory()).get(
            LoginViewModel::class.java
        )
        loginViewModel.setOnIntervalListener(this)
        binding.codeBtn.setOnClickListener {
            if (loginViewModel.regexForm(
                    binding.phone.toString(),
                    binding.code.toString()
                )
            ) loginViewModel.startInterval()
        }
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
}


