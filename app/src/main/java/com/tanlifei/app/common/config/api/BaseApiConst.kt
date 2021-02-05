package com.tanlifei.app.common.config.api

import com.tanlifei.app.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/5 14:40
 */
object BaseApiConst {

    /**————————————————————————————————————————————————— 当前运行环境 ——————————————————————————————————————————————*/
    @DefaultDomain //设置为默认域名
    @JvmField
    var BASE_URL = BuildConfig.BASE_URL


    /**————————————————————————————————————————————————— 开发（DEV）环境  —————————————————————————————————————————*/

    /* 开发环境H5地址 */
    const val URL_BASE_DEV_HELPER = "https://globalh5dev.jinlingkeji.cn/helper#"


    /**————————————————————————————————————————————————— 测试（TEST）环境 —————————————————————————————————————————*/

    /* 测试环境H5地址 */
    const val URL_BASE_TEST_HELPER = "https://globalh5pro.jinlingkeji.cn/helper#"

    /**————————————————————————————————————————————————— 正式（PRO）环境  —————————————————————————————————————————*/

    /* 正式环境H5地址 */
    const val URL_BASE_PRO_HELPER = "https://globalh5test.jinlingkeji.cn/helper#"


    /**
     * 用户协议
     */
    const val URL_AGREEMENT = "https://appoffice.jinlingkeji.cn/#/"
}