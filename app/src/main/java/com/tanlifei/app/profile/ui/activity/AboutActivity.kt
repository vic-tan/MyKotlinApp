package com.tanlifei.app.profile.ui.activity

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.AntiShakeUtils
import com.common.utils.ViewUtils
import com.tanlifei.app.common.config.api.ApiConst
import com.tanlifei.app.databinding.ActivityAboutBinding
import com.tanlifei.app.profile.viewmodel.AboutViewModel

/**
 * @desc:关于界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AboutActivity : BaseToolBarActivity<ActivityAboutBinding, AboutViewModel>(),
    View.OnClickListener {

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(AboutActivity::class.java)
        }
    }

    override fun createViewModel(): AboutViewModel {
        return AboutViewModel()
    }

    override fun init() {
        binding.versionName.text = "当前版本V${AppUtils.getAppVersionName()}"
        binding.logo.setImageResource(EnvironmentUtils.appLogo())
        ViewUtils.setOnClickListener(
            this,
            binding.schoolLeaders,
            binding.userAgreement,
            binding.privateAgreement,
            binding.privacyRights,
            binding.rechargeAgreement,
            binding.lecturerAgreement
        )
    }


    /**
     * webView查看协议
     */
    private fun gotoWeb(title: String?, url: String) {
        BaseWebViewActivity.actionStart(title, url)
    }

    override fun onClick(v: View) {
        if (AntiShakeUtils.isInvalidClick(v)) return
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
                url = ApiConst.URL_RECHARGE_AGREEMENT
            }
            /*讲师入驻协议*/
            binding.lecturerAgreement -> {
                title = binding.lecturerAgreementTitle.text.toString()
                url = ApiConst.URL_LECTURER_AGREEMENT
            }
        }
        url?.let { gotoWeb(title, it) }
    }


}