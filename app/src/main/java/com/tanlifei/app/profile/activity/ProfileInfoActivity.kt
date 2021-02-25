package com.tanlifei.app.profile.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.ComApplication
import com.common.core.base.ui.activity.BaseBVMActivity
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.utils.AntiShakeUtils
import com.common.utils.ComDialogUtils
import com.common.utils.ViewUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivitySettingBinding
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.profile.viewmodel.SettingViewModel


/**
 * @desc:个人资料
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ProfileInfoActivity : BaseBVMActivity<ActivitySettingBinding, SettingViewModel>(),
    View.OnClickListener {


    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(ProfileInfoActivity::class.java)
        }
    }

    override fun createViewModel(): SettingViewModel {
        return SettingViewModel()
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.isToken.observe(this, Observer {
            ComApplication.token = null
            UserInfoUtils.clear()
            LoginAtivity.actionStart()
            ActivityUtils.finishActivity(this)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        ViewUtils.setOnClickListener(this, binding.about, binding.exit)
    }

    private fun initData() {
        binding.versionName.text = "V${AppUtils.getAppVersionName()}"
    }

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return
        when (v) {
            binding.about -> AboutActivity.actionStart()
            binding.exit -> {
                ComDialogUtils.comConfirm(
                    this,
                    "您确定要退出应用吗?",
                    OnConfirmListener {
                        viewModel.requestLogin()
                    })
            }
        }
    }


}