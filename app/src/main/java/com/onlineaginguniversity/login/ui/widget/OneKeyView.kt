package com.onlineaginguniversity.login.ui.widget

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.common.widget.component.extension.color
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.setVisible
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import com.onlineaginguniversity.R
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.login.listener.OnKeyLoginListener
import com.onlineaginguniversity.login.utils.LoginUtils
import org.json.JSONException
import org.json.JSONObject

/**
 * @desc:自定义xml 一键登录View
 * @author: tanlifei
 * @date: 2021/4/13 18:22
 */
class OneKeyView(
    var mAuthHelper: PhoneNumberAuthHelper,
    var listener: OnKeyLoginListener.UIClickListener,
    var isLogin: Boolean = true
) {

    private var protocolPrompt: ImageView? = null
    private var avatar: ImageView? = null
    private var isChecked = false

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
            isChecked = jsonObj!!.optBoolean("isChecked")
            when (code) {
                ResultCode.CODE_ERROR_USER_LOGIN_BTN -> setProtocolPromptVisible(!isChecked)
            }
        }
        mAuthHelper.removeAuthRegisterXmlConfig()
        mAuthHelper.removeAuthRegisterViewConfig()
        mAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.activity_one_key_login, object : AbstractPnsViewDelegate() {
                    override fun onViewCreated(view: View) {
                        avatar = findViewById(R.id.avatar) as ImageView
                        var otherLogin = findViewById(R.id.other_login) as TextView
                        var groupView = findViewById(R.id.group_view) as Group
                        groupView.setVisible(isLogin)
                        if (!isLogin) {
                            otherLogin.text = "其他手机号绑定"
                        }
                        updateLogo()
                        protocolPrompt = findViewById(R.id.protocol_prompt) as ImageView
                        val wxLogin =
                            findViewById(R.id.wx_login) as ImageView
                        findViewById(R.id.change_environment).setOnClickListener {
                            listener.clickEnvironment()
                        }
                        otherLogin.setOnClickListener {
                            setProtocolPromptVisible(false)
                            listener.clickOtherBtn()
                        }
                        wxLogin.setOnClickListener {
                            if (!isChecked) {
                                setProtocolPromptVisible(true)
                            } else {
                                listener.clickWxBtn()
                            }
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
            .setPrivacyBefore("勾选表示已阅读并同意")
            .setAppPrivacyColor(
                Color.GRAY,
                Color.parseColor("#A47E68")
            )
            .setPrivacyOffsetY(420)
            .setCheckedImgPath("ic_onkey_checked")
            .setUncheckedImgPath("ic_one_key_checked_normal")
            .setLogoHidden(true)
            .setNavHidden(true)
            .setLightColor(true)
            .setLogBtnToastHidden(true)
            .setSloganHidden(true)
            .setSwitchAccHidden(true)
            .setPrivacyState(false)
            .setCheckboxHidden(false)
            .setNavReturnHidden(false)
            .setStatusBarColor(Color.WHITE)
            .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LOW_PROFILE)
            .setWebNavReturnImgPath("ic_arrow_web")
            .setWebNavTextSize(17)
            .setWebNavColor(Color.WHITE)
            .setWebNavTextColor(color(R.color.txt_basic))
            .setNumberSize(17)
            .setLogBtnText(if (isLogin) "本机号码一键登录" else "本机号码一键绑定")
            .setLogBtnTextSize(18)
            .setNumberColor(Color.BLACK)
            .setVendorPrivacyPrefix("《")
            .setVendorPrivacySuffix("》")
            .setLogBtnBackgroundPath("login_btn_bg")
            .setScreenOrientation(authPageOrientation).create()
    }

    /**
     * 显示提示协议view
     */
    private fun setProtocolPromptVisible(isVisible: Boolean) {
        when {
            isVisible -> protocolPrompt?.let { LoginUtils.delayedProtocolPrompt(it) }
            else -> protocolPrompt?.let { it.gone() }
        }
    }


    /**
     * 更改环境变更logo
     */
    fun updateLogo() {
        avatar?.let {
            LoginUtils.synchEnvironmentLogo(it)
        }
    }
}