package com.common.core.environment.utils

import com.blankj.utilcode.util.ObjectUtils
import com.common.BuildConfig
import com.common.R
import com.common.cofing.constant.ApiEnvironmentConst
import com.common.core.environment.bean.EnvironmentBean
import com.common.core.environment.bean.ModuleBean
import com.common.utils.MyLogTools

/**
 * @desc:环境切换器设置
 * @author: tanlifei
 * @date: 2021/2/5 16:56
 */
object EnvironmentUtils {

    /**
     * 初始化环境切换器
     */
    fun initEnvironmentSwitcher(): MutableList<ModuleBean> {
        var environmentList: MutableList<ModuleBean> = ArrayList()
        val apiList: MutableList<EnvironmentBean> = ArrayList()
        apiList.add(
            EnvironmentBean(
                "开发环境",
                BuildConfig.BASE_URL_DEV,
                "environment_dev"
            )
        )
        apiList.add(
            EnvironmentBean(
                "测试环境",
                BuildConfig.BASE_URL_TEST,
                "environment_test"
            )
        )
        apiList.add(
            EnvironmentBean(
                "正式环境",
                BuildConfig.BASE_URL_PRO,
                "environment_pro"
            )
        )
        environmentList.add(
            ModuleBean(
                "接口",
                EnvironmentBean.GROUP_API,
                apiList
            )
        )

        val shareList: MutableList<EnvironmentBean> = ArrayList()
        shareList.add(
            EnvironmentBean(
                "开发分享",
                BuildConfig.BASE_URL_DEV + "devShare/",
                defaultCheck = true
            )
        )
        shareList.add(
            EnvironmentBean(
                "测试分享",
                BuildConfig.BASE_URL_TEST + "testShare/"
            )
        )
        shareList.add(
            EnvironmentBean(
                "正式分享",
                BuildConfig.BASE_URL_PRO + "proShare/"
            )
        )
        environmentList.add(
            ModuleBean(
                "分享",
                EnvironmentBean.GROUP_SHARE,
                shareList
            )
        )
        return environmentList
    }

    /**
     * 环境切换修改请求接口路径
     */
    fun onEnvironmentChanged(environment: EnvironmentBean) {
        try {
            if (environment.group == EnvironmentBean.GROUP_API) {
                if (ObjectUtils.isNotEmpty(environment.url)) {
                    ApiEnvironmentConst.BASE_URL = environment.url
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 初始化环境
     */
    fun initBaseApiUrl(currentUrl: String) {
        val apiUrl =
            EnvironmentChangeManager.initEnvironment()
        if (ObjectUtils.isNotEmpty(apiUrl)) {
            ApiEnvironmentConst.BASE_URL = apiUrl!!
        } else {
            ApiEnvironmentConst.BASE_URL =
                when (currentUrl) {
                    BuildConfig.BASE_URL_DEV -> BuildConfig.BASE_URL_DEV
                    BuildConfig.BASE_URL_TEST -> BuildConfig.BASE_URL_TEST
                    else -> BuildConfig.BASE_URL_PRO
                }
        }
    }

    /**
     * 获取appLogo
     */
    fun appLogo(): Int = when (ApiEnvironmentConst.BASE_URL) {
        BuildConfig.BASE_URL_DEV -> R.mipmap.ic_launcher_dev
        BuildConfig.BASE_URL_TEST -> R.mipmap.ic_launcher_test
        else -> R.mipmap.ic_launcher_pro
    }
}