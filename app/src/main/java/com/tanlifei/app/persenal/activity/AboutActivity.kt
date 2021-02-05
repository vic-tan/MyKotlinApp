package com.tanlifei.app.persenal.activity

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.base.ui.activity.BaseToolBarActivity
import com.common.utils.ResUtils
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityAboutBinding
import com.tanlifei.app.databinding.ActivitySettingBinding

/**
 * @desc:关于界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AboutActivity : BaseToolBarActivity<ActivityAboutBinding>() {

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(AboutActivity::class.java)
        }
    }

    override fun init() {
        binding.versionName.text = "当前版本V${AppUtils.getAppVersionName()}"
    }


}