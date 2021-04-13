package com.common.core.share.utils

import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import com.common.ComFun
import com.common.constant.GlobalEnumConst
import com.common.core.share.listener.OnAuthListener
import com.common.widget.component.extension.toast
import java.util.HashMap

/**
 * @desc:第三方登录授权
 * @author: tanlifei
 * @date: 2021/4/13 14:05
 */
object AuthUtils {

    /**
     * 微信授权
     */
    fun wechatAuth(authListener: OnAuthListener) {
        val plat: Platform = ShareSDK.getPlatform(Wechat.NAME)
        //移除授权状态和本地缓存，下次授权会重新授权
        plat.removeAccount(true)
        //SSO授权，传false默认是客户端授权
        plat.SSOSetting(false)
        //授权回调监听，监听oncomplete，onerror，oncancel三种状态 是子线程
        plat.platformActionListener = object : PlatformActionListener {
            override fun onComplete(
                platform: Platform?,
                p1: Int,
                prams: HashMap<String, Any>?
            ) {
                ComFun.mHandler.post {
                    authListener.onComplete(GlobalEnumConst.ShareType.WECHAT, prams)
                }
            }

            override fun onCancel(platform: Platform?, p1: Int) {
                ComFun.mHandler.post {
                    authListener.onCancel(GlobalEnumConst.ShareType.WECHAT)
                    toast("已取消授权")
                }
            }

            override fun onError(platform: Platform?, p1: Int, throws: Throwable?) {
                ComFun.mHandler.post {
                    authListener.onError(GlobalEnumConst.ShareType.WECHAT, throws)
                    toast("授权失败")
                }
            }

        }
        plat.showUser(null)
    }
}