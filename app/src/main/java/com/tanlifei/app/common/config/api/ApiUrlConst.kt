package com.tanlifei.app.common.config.api

import com.common.cofing.constant.BaseApiConst

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/29 16:13
 */
object ApiUrlConst {


    /**
     * 用户协议
     */
    const val URL_USER_AGREEMENT = "${BaseApiConst.URL_AGREEMENT}xieyi"

    /**
     * 隐私政策
     */
    const val URL_PRIVATE_AGREEMENT = "${BaseApiConst.URL_AGREEMENT}yinsi"

    /**
     * 学校领导协议
     */
    const val URL_SCHOOL_LEADERS_AGREEMENT = "${BaseApiConst.URL_AGREEMENT}leader"

    /**
     * 充值购买协议
     */
    const val URL_RECHARGE_AGREEMENT = "${BaseApiConst.URL_AGREEMENT}payMode"

    /**
     * 讲师入驻协议
     */
    const val URL_LECTURER_AGREEMENT = "${BaseApiConst.URL_AGREEMENT}WebPage"


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