package com.tanlifei.app.persenal.activity

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.tanlifei.app.common.config.api.ApiConst
import com.tanlifei.app.databinding.ActivityAboutBinding

/**
 * @desc:关于界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AboutActivity : BaseToolBarActivity<ActivityAboutBinding>(), View.OnClickListener {

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(AboutActivity::class.java)
        }
    }

    override fun init() {
        binding.versionName.text = "当前版本V${AppUtils.getAppVersionName()}"
        binding.schoolLeaders.setOnClickListener(this)
        binding.userAgreement.setOnClickListener(this)
        binding.privateAgreement.setOnClickListener(this)
        binding.privacyRights.setOnClickListener(this)
        binding.rechargeAgreement.setOnClickListener(this)
        binding.lecturerAgreement.setOnClickListener(this)
    }


    /**
     * webView查看协议
     */
    private fun gotoWeb(title: String?, url: String) {
        BaseWebViewActivity.actionStart(this, title, url)
    }

    override fun onClick(v: View) {
        var title: String? = ""
        var url: String? = ""
        when (v) {
            /*学校领导*/
            binding.schoolLeaders -> {
                title = binding.schoolLeadersTitle.text.toString()
                url = ApiConst.URL_SCHOOL_LEADERS_AGREEMENT
            }
            /*用户协议*/
            binding.userAgreement -> {
                title = binding.userAgreementTitle.text.toString()
                url = ApiConst.URL_USER_AGREEMENT
            }
            /*隐私政策*/
            binding.privateAgreement -> {
                title = binding.privateAgreementTitle.text.toString()
                url = ApiConst.URL_PRIVATE_AGREEMENT
            }
            /*隐私权限*/
            binding.privacyRights -> {
                title = binding.privacyRightsTitle.text.toString()
                url = ApiConst.URL_PRIVATE_AGREEMENT
            }
            /*充值购买协议*/
            binding.rechargeAgreement -> {
                title = binding.rechargeAgreementTitle.text.toString()
                url = ApiConst.URL_PRIVATE_AGREEMENT
            }
            /*讲师入驻协议*/
            binding.lecturerAgreement -> {
                title = binding.lecturerAgreementTitle.text.toString()
                url = ApiConst.URL_PRIVATE_AGREEMENT
            }
        }
        url?.let { gotoWeb(title, it) }
    }


}