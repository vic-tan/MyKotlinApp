package com.tanlifei.app

import com.common.ComApplication
import com.common.core.http.RxHttpManager


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class BaseApplication : ComApplication() {

    override fun onCreate() {
        super.onCreate()
        RxHttpManager.init(this)
    }


}