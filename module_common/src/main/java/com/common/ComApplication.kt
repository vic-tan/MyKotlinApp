package com.common

import android.app.Application
import android.content.Context
import com.common.utils.PictureSelectorEngineImp
import com.luck.picture.lib.app.IApp
import com.luck.picture.lib.app.PictureAppMaster
import com.luck.picture.lib.engine.PictureSelectorEngine


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class ComApplication : Application(),IApp {

    //static 代码段可以防止内存泄露
    init {
        ComFun.init()
    }

    override fun onCreate() {
        super.onCreate()
        ComFun.initialize(this)
        PictureAppMaster.getInstance().app = this
    }

    override fun getAppContext(): Context {
        return this
    }

    override fun getPictureSelectorEngine(): PictureSelectorEngine {
        return PictureSelectorEngineImp()
    }

}


