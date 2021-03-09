package com.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.common.core.base.bean.UserBean
import com.common.core.environment.utils.EnvironmentChangeManager
import com.common.core.http.RxHttpManager
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal
import update.UpdateAppUtils


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class ComApplication : Application() {

    //static 代码段可以防止内存泄露
    init {
        ComFun.init()
    }

    override fun onCreate() {
        super.onCreate()
        ComFun.initialize(this)
    }

}


