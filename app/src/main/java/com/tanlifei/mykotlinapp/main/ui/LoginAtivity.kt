package com.tanlifei.mykotlinapp.main.ui

import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.databinding.ActivityLoginBinding
import com.tanlifei.mykotlinapp.home.ui.activity.HomeActivity

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/26 17:37
 */
open class LoginAtivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun layoutResId(): Int {
        return R.layout.activity_login
    }

    override fun bindView() {
        binding = ActivityLoginBinding.bind(containerView)
    }

    override fun initView() {

        binding.login.setOnClickListener { ActivityUtils.startActivity(HomeActivity::class.java) }
    }
}