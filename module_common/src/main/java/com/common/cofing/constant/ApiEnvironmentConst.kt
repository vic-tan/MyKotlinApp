package com.common.cofing.constant

import com.common.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @desc: 各环境域名定义
 * @author: tanlifei
 * @date: 2021/2/5 14:40
 */
object ApiEnvironmentConst {

    /**————————————————————————————————————————————————— 当前运行环境 ——————————————————————————————————————————————*/
    @DefaultDomain //设置为默认域名
    @JvmField
    var BASE_URL = BuildConfig.BASE_URL_PRO

    /* H5地址 */
    val URL_BASE_HELPER = getBaseHelperUrl()


    /**————————————————————————————————————————————————— 开发（DEV）环境  —————————————————————————————————————————*/

    /* 开发环境H5地址 */
    private const val URL_BASE_DEV_HELPER = "https://globalh5dev.jinlingkeji.cn/helper#"


    /**————————————————————————————————————————————————— 测试（TEST）环境 —————————————————————————————————————————*/

    /* 测试环境H5地址 */
    private const val URL_BASE_TEST_HELPER = "https://globalh5test.jinlingkeji.cn/helper#"

    /**————————————————————————————————————————————————— 正式（PRO）环境  —————————————————————————————————————————*/

    /* 正式环境H5地址 */
    private const val URL_BASE_PRO_HELPER = "https://globalh5pro.jinlingkeji.cn/helper#"


    /**
     * 根据BASE_URL 获取对应的H5地址
     */
    private fun getBaseHelperUrl(): String {
        return when (BASE_URL) {
            BuildConfig.BASE_URL_DEV -> URL_BASE_DEV_HELPER
            BuildConfig.BASE_URL_TEST -> URL_BASE_TEST_HELPER
            BuildConfig.BASE_URL_PRO -> URL_BASE_PRO_HELPER
            else -> URL_BASE_PRO_HELPER
        }
    }

}