package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.ComFun
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.utils.ComDialogUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.startActivity
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivitySettingBinding
import com.tanlifei.app.main.ui.activity.LoginAtivity
import com.tanlifei.app.profile.viewmodel.SettingViewModel


/**
 * @desc:设置界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class SettingActivity : BaseToolBarActivity<ActivitySettingBinding, SettingViewModel>() {


    companion object {
        fun actionStart() {
            startActivity<SettingActivity> { }
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
        mViewModel.mIsToken.observe(this, Observer {
            ComFun.mToken = null
            UserInfoUtils.clear()
            LoginAtivity.actionStart()
            ActivityUtils.finishActivity(this)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(mBinding.about, mBinding.exit,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.about -> AboutActivity.actionStart()
                    mBinding.exit -> {
                        ComDialogUtils.comConfirm(
                            this,
                            "您确定要退出应用吗?",
                            OnConfirmListener {
                                mViewModel.requestLogin()
                            })
                    }
                }
            })
    }

    private fun initData() {
        mBinding.versionName.text = "V${AppUtils.getAppVersionName()}"
    }
}