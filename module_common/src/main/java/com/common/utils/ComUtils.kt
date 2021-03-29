package com.common.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.base.bean.UpdateAppBean
import com.common.core.environment.utils.EnvironmentChangeManager
import com.common.widget.component.extension.toast
import com.hjq.toast.ToastUtils
import constant.UiType
import listener.OnBtnClickListener
import listener.OnInitUiListener
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils
import java.util.*

/**
 * @desc: object 修饰类：被object修饰的类为单例模式
 * @author: tanlifei
 * @date: 2021/1/28 17:33
 */
object ComUtils {
    private var mIsExit = false

    /**
     * 退出App
     */
    fun exitApp() {
        var tExit: Timer?
        if (!mIsExit) {
            mIsExit = true // 准备退出
            toast(R.string.app_exit)
            tExit = Timer()
            tExit.schedule(object : TimerTask() {
                override fun run() {
                    mIsExit = false // 取消退出
                }
            }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            //finish所有页面和kill app
            ToastUtils.cancel()
            EnvironmentChangeManager.startEnvironmentSwitchIcon()
            ActivityUtils.finishAllActivities()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    /**
     * 获取默认渠道
     */
    fun getDefaultChannel(context: Context): String? {
        var appInfo: ApplicationInfo?
        var channelIdStr: String?
        try {
            appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val channelId = appInfo.metaData["CHANNEL"]
            channelIdStr =
                if (TextUtils.isEmpty(channelId.toString())) "dev" else channelId.toString()
        } catch (e: Exception) {
            channelIdStr = "qa"
            e.printStackTrace()
        }
        return channelIdStr
    }

    /**
     * 应用升级显示
     */
    fun udpateApp(updateAppBean: UpdateAppBean) {
        if (ObjectUtils.isEmpty(updateAppBean)) {
            return
        }
        if (ObjectUtils.isEmpty(updateAppBean.downloadUrl)) {
            return
        }

        // 更新配置
        val updateConfig = UpdateConfig().apply {
            force = updateAppBean.updateInstall == 1
            showDownloadingToast = false
            notifyImgRes = R.mipmap.ic_launcher
            apkSaveName = "update_app_${updateAppBean.updateVersion}_${System.currentTimeMillis()}"
        }
        UpdateAppUtils
            .getInstance()
            .apkUrl(updateAppBean.downloadUrl)
            .updateTitle("版本更新啦")
            .updateConfig(updateConfig)
            .updateContent(updateAppBean.updateLog)
            .uiConfig(UiConfig(uiType = UiType.CUSTOM, customLayoutId = R.layout.dialog_update_app))
            .setOnInitUiListener(object : OnInitUiListener {
                override fun onInitUpdateUi(
                    view: View?,
                    updateConfig: UpdateConfig,
                    uiConfig: UiConfig
                ) {
                    view?.findViewById<TextView>(R.id.tv_version_name)?.text =
                        "V${updateAppBean.clientVersion}"
                }
            })
            // 设置 立即更新 按钮点击事件
            .setUpdateBtnClickListener(object : OnBtnClickListener {
                override fun onClick(): Boolean {
                    toast("正在下载中...")
                    return false // 事件是否消费，是否需要传递下去。false-会执行原有点击逻辑，true-只执行本次设置的点击逻辑
                }
            })
            .update()
    }

}