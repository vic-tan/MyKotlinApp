package com.onlineaginguniversity.login.ui.widget

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.common.widget.component.extension.color
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.visible
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.ui.activity.InputPhoneAtivity
import org.json.JSONException
import org.json.JSONObject

/**
 * @desc:自定义xml 一键登录View
 * @author: tanlifei
 * @date: 2021/4/13 18:22
 */
class CustomOneKeyLoginView(
    var mAuthHelper: PhoneNumberAuthHelper,
    var listener: OnKeyLoginListener.UIClickListener
) {

    private lateinit var protocolHint: ImageView

    fun configAuthPage() {
        mAuthHelper.setUIClickListener { code: String?, _, jsonString: String? ->
            var jsonObj: JSONObject? = null
            try {
                if (!TextUtils.isEmpty(jsonString)) {
                    jsonObj = JSONObject(jsonString)
                }
            } catch (e: JSONException) {
                jsonObj = JSONObject()
            }
            when (code) {
                ResultCode.CODE_ERROR_USER_LOGIN_BTN, ResultCode.CODE_ERROR_USER_CHECKBOX -> if (!jsonObj!!.optBoolean(
                        "isChecked"
                    )
                ) {
                    protocolHint.visible()
                } else {
                    protocolHint.gone()
                }
            }
        }
        mAuthHelper.removeAuthRegisterXmlConfig()
        mAuthHelper.removeAuthRegisterViewConfig()
        mAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.activity_one_key_login, object : AbstractPnsViewDelegate() {
                    override fun onViewCreated(view: View) {
                        protocolHint = findViewById(R.id.protocol_hint) as ImageView
                        val wxLogin =
                            findViewById(R.id.wx_login) as ImageView
                        findViewById(R.id.change_environment).setOnClickListener {
                            listener.clickEnvironment()
                        }
                        findViewById(R.id.other_login).setOnClickListener {
                            protocolHint.gone()
                            InputPhoneAtivity.actionStart(EnumConst.SMSType.MOBILE_LOGIN)
                        }
                        wxLogin.setOnClickListener {
                            protocolHint.gone()
                            listener.clickWxBtn()
                        }
                    }
                })
                .build()
        )
        //图片路径一定要放到drawable 下面，否则无效果
        mAuthHelper.setAuthUIConfig(loginCreate())
    }

    private fun loginCreate(): AuthUIConfig? {
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }
        return AuthUIConfig.Builder().setAppPrivacyOne("《用户协议》", ApiUrlConst.URL_USER_AGREEMENT)
            .setAppPrivacyTwo("《隐私政策》", ApiUrlConst.URL_PRIVATE_AGREEMENT)
            .setAppPrivacyColor(
                Color.GRAY,
                Color.parseColor("#A47E68")
            )
            .setPrivacyOffsetY(420)
            .setCheckedImgPath("icon_group_checked")
            .setUncheckedImgPath("icon_group_normal")
            .setLogoHidden(true)
            .setNavHidden(true)
            .setLightColor(true)
            .setLogBtnToastHidden(true)
            .setSloganHidden(true)
            .setSwitchAccHidden(true)
            .setPrivacyState(false)
            .setCheckboxHidden(false)
            .setNavReturnHidden(false)
            .setNavReturnImgHeight(23)
            .setNavReturnImgWidth(15)
            .setStatusBarColor(Color.WHITE)
            .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LOW_PROFILE)
            .setWebNavReturnImgPath("ic_arrow_web_back")
            .setWebNavTextSize(17)
            .setWebNavColor(Color.WHITE)
            .setWebNavTextColor(color(R.color.txt_basic))
            .setNumberSize(17)
            .setLogBtnText("本机号码一键登录")
            .setLogBtnTextSize(18)
            .setNumberColor(Color.BLACK)
            .setAuthPageActIn("in_activity", "out_activity")
            .setAuthPageActOut("in_activity", "out_activity")
            .setVendorPrivacyPrefix("《")
            .setVendorPrivacySuffix("》")
            .setLogoImgPath("ic_launcher")
            .setLogBtnBackgroundPath("login_btn_bg")
            .setScreenOrientation(authPageOrientation).create()
    }
}