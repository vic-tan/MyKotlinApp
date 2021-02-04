package com.tanlifei.app.common.config

import com.tanlifei.app.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/29 16:13
 */
object UrlConst {

    @DefaultDomain //设置为默认域名
    @JvmField
    var BASE_URL = BuildConfig.BASE_URL


    /**
     * 开发环境
     */
    const val BASE_URL_DEV = BuildConfig.BASE_URL_DEV

    /**
     * 测试环境
     */

    const val BASE_URL_TEST = BuildConfig.BASE_URL_TEST

    /**
     * 正式环境
     */
    const val BASE_URL_PRO = BuildConfig.BASE_URL_PRO


    /**
     * 用户协议
     */
    const val URL_REGISTER_AGREEMENT = "https://www.9bao.tv/mod/static/registerAgreementNew.html"

    /**
     * 隐私政策
     */
    const val URL_AGREEMENT = "https://www.9bao.tv/webview/app/shop/agreement?active=2"


    /**
     * 发送短信验证码
     */
    const val URL_SEND_SMS = "auth/sendsms"

    /**
     * 验证验证码登录
     */
    const val URL_LOGIN = "auth/login"


    /**
     * 开屏广告
     */
    const val URL_ADS = "message/api/ads/open/screen"
}