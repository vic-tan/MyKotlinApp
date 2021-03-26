package com.tanlifei.app.profile.ui.activity

import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.extension.clickListener
import com.common.utils.extension.startActivity
import com.tanlifei.app.common.config.api.ApiUrlConst
import com.tanlifei.app.databinding.ActivityAboutBinding
import com.tanlifei.app.profile.viewmodel.AboutViewModel

/**
 * @desc:关于界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class AboutActivity : BaseToolBarActivity<ActivityAboutBinding, AboutViewModel>() {

    companion object {
        fun actionStart() {
            startActivity<AboutActivity> { }
        }
    }

    override fun createViewModel(): AboutViewModel {
        return AboutViewModel()
    }

    override fun init() {
        mBinding.versionName.text = "当前版本V${AppUtils.getAppVersionName()}"
        mBinding.logo.setImageResource(EnvironmentUtils.appLogo())
        clickListener(
            mBinding.schoolLeaders,
            mBinding.userAgreement,
            mBinding.privateAgreement,
            mBinding.privacyRights,
            mBinding.rechargeAgreement,
            mBinding.lecturerAgreement,
            clickListener = View.OnClickListener { it ->
                var title: String? = ""
                var url: String? = ""
                when (it) {
                    /*学校领导*/
                    mBinding.schoolLeaders -> {
                        title = mBinding.schoolLeadersTitle.text.toString()
                        url = ApiUrlConst.URL_SCHOOL_LEADERS_AGREEMENT
                    }
                    /*用户协议*/
                    mBinding.userAgreement -> {
                        title = mBinding.userAgreementTitle.text.toString()
                        url = ApiUrlConst.URL_USER_AGREEMENT
                    }
                    /*隐私政策*/
                    mBinding.privateAgreement -> {
                        title = mBinding.privateAgreementTitle.text.toString()
                        url = ApiUrlConst.URL_PRIVATE_AGREEMENT
                    }
                    /*隐私权限*/
                    mBinding.privacyRights -> {
                        title = mBinding.privacyRightsTitle.text.toString()
                        url = ApiUrlConst.URL_PRIVATE_AGREEMENT
                    }
                    /*充值购买协议*/
                    mBinding.rechargeAgreement -> {
                        title = mBinding.rechargeAgreementTitle.text.toString()
                        url = ApiUrlConst.URL_RECHARGE_AGREEMENT
                    }
                    /*讲师入驻协议*/
                    mBinding.lecturerAgreement -> {
                        title = mBinding.lecturerAgreementTitle.text.toString()
                        url = ApiUrlConst.URL_LECTURER_AGREEMENT
                    }
                }
                url?.let { gotoWeb(title, it) }
            }
        )
    }


    /**
     * webView查看协议
     */
    private fun gotoWeb(title: String?, url: String) {
        BaseWebViewActivity.actionStart(title, url)
    }


}