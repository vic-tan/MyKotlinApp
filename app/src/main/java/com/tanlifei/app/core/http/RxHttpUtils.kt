package com.tanlifei.app.core.http

import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.rxlife.coroutine.RxLifeScope
import kotlinx.coroutines.CoroutineScope

object RxHttpUtils {

    /**
     *不在Activity/Fragment/ViewMode 中请用上方法请求网络
     */
    fun start(block: suspend CoroutineScope.() -> Unit) {
        start(RxLifeScope(), block)
    }

    /**
     * 在Activity/Fragment/ViewMode 中请用上方法请求网络
     */
    fun start(rxLifeScope: RxLifeScope, block: suspend CoroutineScope.() -> Unit) {
        rxLifeScope.launch(block,
            {
                //异常回调，这里可以拿到Throwable对象
                it.show(it.errorCode, it.errorMsg)
            }, {
                //开始回调，可以开启等待弹窗
            }, {
                //结束回调，可以销毁等待弹窗
            })
    }
}