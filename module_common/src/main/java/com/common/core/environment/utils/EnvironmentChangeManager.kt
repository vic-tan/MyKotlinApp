package com.common.core.environment.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComApplication
import com.common.core.environment.bean.EnvironmentBean
import org.litepal.LitePal

/**
 * @desc:切换应用图标类
 * @author: tanlifei
 * @date: 2021/2/3 14:49
 */
object EnvironmentChangeManager {
    var environmentMap = mutableMapOf<String, String>()

    init {
        environmentMap["dalt"] = ".SplashActivity"
        environmentMap["environment_dev"] = ".environment_dev"
        environmentMap["environment_test"] = ".environment_test"
        environmentMap["environment_pro"] = ".environment_pro"
    }

    /**
     * 更换应用icon
     */
    private fun changeIcon(
        context: Context?,
        iconType: String?
    ): Boolean {
        /**
         * 业务逻辑：根据接口返回数据，决定需要更换为哪个icon
         */
        return if (context == null) {
            false
        } else enableComponent(
            context,
            false,
            iconType
        )

    }

    /**
     * 判断 component 是否可用
     */
    private fun componentEnabled(
        context: Context,
        activityPath: String?
    ): Boolean {
        if (context == null) {
            return false
        }
        val pm = context.packageManager
        val cn = ComponentName(context, context.packageName + activityPath)
        return pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    /**
     * 启用组件
     *
     * @param force    设置为false，可能会导致应用在一段时间后退出
     * @param iconType 需要和数组索引保持一致
     */
    private fun enableComponent(
        context: Context?,
        force: Boolean,
        iconType: String?
    ): Boolean {
        if (context == null || ObjectUtils.isEmpty(environmentMap)) {
            return false
        }
        if (!environmentMap.containsKey(iconType)) {
            return false
        }
        val activityPath = environmentMap[iconType]
        if (componentEnabled(
                context,
                activityPath
            )
        ) {
            return false
        }
        val enableFlag = if (force) 0 else PackageManager.DONT_KILL_APP
        try {
            val packageManager = context.packageManager
            // 先禁用所有不需要启用的其它组件
            for (value in environmentMap.values) {
                if (value == activityPath) {
                    continue
                }
                packageManager.setComponentEnabledSetting(
                    ComponentName(context, context.packageName + value),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                )
            }
            // 再启用需要启用的组件
            packageManager.setComponentEnabledSetting(
                ComponentName(context, context.packageName + activityPath),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, enableFlag
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 开始修改应用图标
     */
    fun startEnvironmentSwitchIcon() {
        try {
            val saveEnvironmentList =
                LitePal.where("${EnvironmentBean.DB_GROUP} = ? ", "${EnvironmentBean.GROUP_API}")
                    .find(EnvironmentBean::class.java)

            if (ObjectUtils.isEmpty(saveEnvironmentList)) {
                return
            }
            val bean: EnvironmentBean = saveEnvironmentList[saveEnvironmentList.size - 1]
            if (bean.activityAlias.isNotEmpty()) {
                if (ObjectUtils.isNotEmpty(bean.activityAlias)) {
                    changeIcon(
                        ComApplication.context,
                        bean.activityAlias
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化环境切换器，一般在应用启动设置
     */
    fun initEnvironment(): String? {
        try {
            val saveEnvironmentList =
                LitePal.where("${EnvironmentBean.DB_GROUP} = ? ", "${EnvironmentBean.GROUP_API}")
                    .find(EnvironmentBean::class.java)
            if (ObjectUtils.isEmpty(saveEnvironmentList)) {
                return null
            }
            val environment: EnvironmentBean = saveEnvironmentList[saveEnvironmentList.size - 1]
            if (environment.group == EnvironmentBean.GROUP_API) {
                if (ObjectUtils.isNotEmpty(environment.url)) {
                    return environment.url
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}