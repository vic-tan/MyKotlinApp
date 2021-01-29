package com.tanlifei.app.common.config

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/29 16:13
 */
object UrlConst {

    @DefaultDomain //设置为默认域名
    @JvmField var URL_BASE = "https://gatewaytest.jinlingkeji.cn/v9/"

    /**
     * 正式环境
     */
    const val URL_BASE_PRO = "https://jlkjapp.jinlingkeji.cn/v9/"

    /**
     * 开发环境
     */
    const val URL_BASE_DEV = "https://gateway.jinlingkeji.cn/"

    /**
     * 测试环境
     */

    const val URL_BASE_TEST = "https://gatewaytest.jinlingkeji.cn/v9/"

    /**
     * 发送短信验证码
     */
    const val URL_SEND_SMS = "auth/sendsms"

}