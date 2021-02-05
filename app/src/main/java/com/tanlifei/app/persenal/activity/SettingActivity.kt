package com.tanlifei.app.persenal.activity

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.tanlifei.app.databinding.ActivitySettingBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class SettingActivity : BaseToolBarActivity<ActivitySettingBinding>() {

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(SettingActivity::class.java)
        }
    }

    override fun initView() {
        binding.versionName.text = AppUtils.getAppVersionName()
    }


}